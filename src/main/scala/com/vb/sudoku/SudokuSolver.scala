package com.vb.sudoku

object SudokuSolver extends App {
  val sudoku = "4 8 5 0 6 9 0 0 0 1 6 7 3 0 5 0 0 0 9 2 0 0 0 7 0 4 0 0 4 8 0 2 0 0 6 9 5 0 0 0 9 0 0 0 1 6 9 0 0 8 0 3 7 0 0 3 0 1 0 0 0 5 6 0 0 0 9 0 2 7 1 4 0 0 0 4 5 0 8 2 3"
  val field = sudoku.split(" ")
    .map(_.toInt)
    .zipWithIndex
    .filter(_._1 > 0)
    .foldLeft(new Field())((field, input) => field.setValue(input._2 / 9, input._2 % 9, input._1))
  val f = solve(field)
  f.print()

  def solve(field: Field): Field = {
    if (field.isWrong || field.isSolved) {
      field
    } else {
      val nextCell = field.getCell
      for (value <- nextCell.availableValues) {
        val f = solve(field.setValue(nextCell.row, nextCell.col, value))
        if (f.isSolved) return f
      }
      field
    }
  }

}

case class Cell(row: Int, col: Int, value: Option[Int], availableValues: Array[Int]) {
  val sector: Int = resolveSector

  private def resolveSector: Int = {
    if (row >=0 && row <=2 && col >=0 && col <= 2) 1
    else if (row >=0 && row <=2 && col >=3 && col <= 5) 2
    else if (row >=0 && row <=2 && col >=6 && col <= 8) 3
    else if (row >=3 && row <=5 && col >=0 && col <= 2) 4
    else if (row >=3 && row <=5 && col >=3 && col <= 5) 5
    else if (row >=3 && row <=5 && col >=6 && col <= 8) 6
    else if (row >=6 && row <=8 && col >=0 && col <= 2) 7
    else if (row >=6 && row <=8 && col >=3 && col <= 5) 8
    else 9
  }
}

class Field {
  private[this] val ERROR_CELL = Cell(-1, -1, None, Array())
  private[this] val initialValues = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
  private var cells: Array[Cell] = for (i <- (0 until 9).toArray; j <- 0 until 9) yield Cell(i, j, None, initialValues)

  def this(newCells: Array[Cell]) {
    this()
    this.cells = newCells
  }

  def print(): Unit = {
    for (i <- (0 until 9).toArray; j <- 0 until 9) {
      if (j == 0) {
        println
      }
      val value = cells(toIndex(i, j)).value match {
        case Some(v) => v
        case None =>  0
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