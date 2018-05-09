package com.vb.sudoku.domain

class Field {
  private[this] val ERROR_CELL = Cell(-1, -1, None, Array())
  private[this] val INITIAL_VALUES = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
  private var cells: Array[Cell] = for (i <- (0 until 9).toArray; j <- 0 until 9) yield Cell(i, j, None, INITIAL_VALUES)

  def this(newCells: Array[Cell]) {
    this()
    this.cells = newCells
  }

  def print(pretty: Boolean = true): Unit = {
    for (i <- (0 until 9).toArray; j <- 0 until 9) {
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
      if (c eq cell) Cell(c.row, c.col, Some(value), Array())
      else if (c.row == row || c.col == col || c.sector == cell.sector) {
        if (c.value.getOrElse(0) == value) ERROR_CELL
        else Cell(c.row, c.col, c.value, for (i <- c.availableValues if i != value) yield i)
      } else c
    }
    new Field(newCells)
  }

  def isSolved: Boolean = {
    !cells.exists(_.value.isEmpty)
  }

  def isWrong: Boolean = {
    cells.exists(_ eq ERROR_CELL)
  }

  def getCell: Cell = {
    cells.filter(_.value.isEmpty).map(c => (c, c.availableValues.length)).minBy(_._2)._1
  }

  private def toIndex(row: Int, col: Int): Int = {
    row * 9 + col
  }
}

object Field {
  def apply(str: String): Field = {
    str.split(" ")
      .map(_.toInt)
      .zipWithIndex
      .filter(_._1 > 0)
      .foldLeft(new Field())((field, input) => field.setValue(input._2 / 9, input._2 % 9, input._1))
  }
}
