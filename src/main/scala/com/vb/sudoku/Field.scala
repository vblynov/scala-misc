package com.vb.sudoku

case class Cell(row: Int, col: Int, value: Option[Int], availableValues: Array[Int], sector: Int)

class Field(cells : Array[Cell]) {
  private[this] val size = Math.sqrt(cells.length).toInt

  def print(pretty: Boolean = true): Unit = {
    for (i <- (0 until size).toArray; j <- 0 until size) {
      if (j == 0 && pretty) {
        println
      }
      val value = cells(toIndex(i, j)).value match {
        case Some(v) => v
        case None => 0
      }
      System.out.print(value + " ")
    }
  }

  def setValue(row: Int, col: Int, value: Int): Field = {
    val cell = cells(toIndex(row, col))
    val newCells = for (c <- cells) yield {
      if (c eq cell) Cell(c.row, c.col, Some(value), Array(), Field.resolveSector(c.row, c.col, size))
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

  def toArray: Array[Int] = cells.map(_.value.getOrElse(0))

  private def toIndex(row: Int, col: Int): Int = {
    row * size + col
  }
}

object Field {
  private val ERROR_CELL = Cell(-1, -1, None, Array(), -1)

  def apply(size: Int): Field = {
    val initialVlaues = (for (i <- 1 to size) yield i).toArray
    val initialCells = for (i <- (0 until size).toArray; j <- 0 until size) yield Cell(i, j, None, initialVlaues, resolveSector(i, j, size))
    new Field(initialCells)
  }

  def apply(str: String): Field = {
    val values = str.split(" ").map(_.toInt)
    val size = Math.sqrt(values.length).toInt
    values.zipWithIndex
      .filter(_._1 > 0)
      .foldLeft(apply(size))((field, input) => field.setValue(input._2 / size, input._2 % size, input._1))
  }

  def resolveSector(row: Int, col: Int, size: Int): Int = {
    val sqSize = Math.sqrt(size).toInt
    sqSize * (row / sqSize) + (col / sqSize) + 1
  }
}
