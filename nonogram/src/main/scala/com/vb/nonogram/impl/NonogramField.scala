package com.vb.nonogram.impl

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class NonogramField private[NonogramField](val rows: Int,
                                           val cols: Int,
                                           private val body: ArrayBuffer[Cell],
                                           private val rowGroups: ArrayBuffer[Position],
                                           private val colGroups: ArrayBuffer[Position]) {


  def cell(row: Int, col: Int): Cell = body(row*cols + col)

  def rowGroup(row: Int): Position = rowGroups(row)

  def colGroup(col: Int): Position = colGroups(col)

  def fillCells(cells: Seq[Cell]): Unit = {
    for (cell <- cells) {
      body(cell.row*cols + cell.col) = Cell(cell.row, cell.col, filled = true)
    }
  }

  private def getFilledColIndices(col: Int): Seq[Int] = {
    for (i <- col to rows*cols by cols if body(i).filled) yield body(i).col
  }

  private def getFilledRowIndices(row: Int): Seq[Int] = {
    for (i <- row*cols to row*cols + cols if body(i).filled) yield body(i).row
  }


}

object NonogramField {
  def apply(fileName: String): NonogramField = {
    val source = Source.fromResource(fileName)
    val lines = source.getLines().toArray
    val rows = lines(0).toInt
    val rowGroup = new ArrayBuffer[Position](rows)
    for (i <- 0 until rows) {
      rowGroup(i) = Position(rows, lines(i + 1).split(" ").map(_.toInt))
    }
    val cols = lines(rows + 1).toInt
    val colGroup = new ArrayBuffer[Position](cols)
    for (i <- 0 until cols) {
      colGroup(i) = Position(cols, lines(i + rows + 1).split(" ").map(_.toInt))
    }
    val body = new ArrayBuffer[Cell](rows * cols)
    for (i <- 0 until rows*cols) {
      body(i) = Cell(i / cols, i % cols)
    }
    new NonogramField(rows, cols, body, rowGroup, colGroup)
  }
}
