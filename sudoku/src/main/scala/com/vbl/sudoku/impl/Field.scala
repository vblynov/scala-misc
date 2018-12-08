package com.vbl.sudoku.impl

case class Cell(row: Int, col: Int, value: Option[Int], availableValues: Array[Int], sector: Int)

class Field private[Field](cells : Array[Cell]) {
  private[this] val size = Math.sqrt(cells.length).toInt

  def setValue(row: Int, col: Int, value: Int): Field = {
    val cell = cells(toIndex(row, col))
    val newCells = for (c <- cells) yield {
      if (c eq cell) {
        if (cell.availableValues.contains(value)) Cell(c.row, c.col, Some(value), Array(), Field.resolveSector(c.row, c.col, size))
        else Field.ERROR_CELL
      }
      else if (c.row == row || c.col == col || c.sector == cell.sector) {
        if (c.value.getOrElse(0) == value) Field.ERROR_CELL
        else Cell(c.row, c.col, c.value, for (i <- c.availableValues if i != value) yield i, Field.resolveSector(c.row, c.col, size))
      } else c
    }
    new Field(newCells)
  }

  def isSolved: Boolean = {
    !cells.exists(_.value.isEmpty)
  }

  def isWrong: Boolean = {
    cells.exists(_ eq Field.ERROR_CELL)
  }

  def getMinCell: Cell = {
    cells.filter(_.value.isEmpty).map(c => (c, c.availableValues.length)).minBy(_._2)._1
  }

  override def toString: String = cells.map(cell => cell.value match {
    case Some(v) => v.toString
    case None => "0"
  }).mkString(" ")

  def toPrettyString: String = cells.map(cell => cell.value match {
    case Some(v) => v.toString
    case None => "0"
  }).grouped(size).map(_.mkString(" ")).mkString("\n")

  private def toIndex(row: Int, col: Int): Int = {
    row * size + col
  }
}

object Field {
  private val ERROR_CELL = Cell(-1, -1, None, Array(), -1)

  def apply(size: Int): Field = {
    if (size <= 0) {
      throw new IllegalArgumentException("Incorrect puzzle size")
    }
    val initialValues = (for (i <- 1 to size) yield i).toArray
    val initialCells = for (i <- (0 until size).toArray; j <- 0 until size) yield Cell(i, j, None, initialValues, resolveSector(i, j, size))
    new Field(initialCells)
  }

  def apply(values: Array[Int]): Field = {
    def toRowCol(index: Int, size: Int): (Int, Int) = {
      (index / size, index % size)
    }
    verifySize(values.length)
    val size = Math.sqrt(values.length).toInt
    val cells = values.zipWithIndex.map(value => {
      val position = toRowCol(value._2, size)
      val cellValue = if (value._1 > 0) Some(value._1) else None
      val availableValues = if (value._1 > 0) (for (i <- 1 to size if i != value._1) yield i).toArray else (for (i <- 1 to size) yield i).toArray
      Cell(position._1, position._2, cellValue, availableValues, resolveSector(position._1, position._2, size))
    })
    new Field(cells)
  }

  def apply(str: String): Field = {
    val values = str.split(" ").map(_.toInt)
    val puzzleSize = values.length
    verifySize(puzzleSize)
    val size = Math.sqrt(puzzleSize).toInt
    values.zipWithIndex
      .filter(_._1 > 0)
      .foldLeft(apply(size))((field, input) => field.setValue(input._2 / size, input._2 % size, input._1))
  }

  def resolveSector(row: Int, col: Int, size: Int): Int = {
    val sqSize = Math.sqrt(size).toInt
    sqSize * (row / sqSize) + (col / sqSize) + 1
  }

  private[this] def verifySize(puzzleSize: Int): Unit = {
    if (puzzleSize <= 0 || Math.pow(puzzleSize, 0.25) % 1 != 0) throw new IllegalArgumentException("Incorrect puzzle size")
  }
}
