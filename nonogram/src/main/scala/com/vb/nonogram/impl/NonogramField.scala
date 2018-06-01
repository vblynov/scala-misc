package com.vb.nonogram.impl

import scala.util.Random

object Cell extends Enumeration {
  val EMPTY, FILLED, EXCLUDED = Value
}

class NonogramField private[NonogramField](private val body: Array[Array[Cell.Value]],
                                           private val rowGroups: Array[Array[Int]],
                                           private val colGroups: Array[Array[Int]]) {

  def getRows: Int = body.length

  def getCols: Int = body(0).length

  def getCell(row: Int, col: Int): Cell.Value = body(row)(col)

  def getRowGroup(row: Int): Array[Int] = rowGroups(row)

  def getColGroup(col: Int): Array[Int] = colGroups(col)

}

object NonogramField {
  def apply(rows: Int, cols: Int): NonogramField = {
    val body = Array.tabulate(rows, cols)((i, j) => if (i == j) Cell.FILLED else Cell.EMPTY)

    val colGroups = for (i <- Array.range(0, cols)) yield {
      val size = Random.nextInt(8) + 1
      for (i <- Array.range(0, size)) yield Random.nextInt(cols - 2) + 1
    }

    val rowGroups = for (i <- Array.range(0, rows)) yield {
      val size = Random.nextInt(8) + 1
      for (i <- Array.range(0, size)) yield Random.nextInt(rows - 2) + 1
    }

    new NonogramField(body, rowGroups, colGroups)
  }
}
