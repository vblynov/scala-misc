package com.vb.nonogram.impl

object Cell extends Enumeration {
  val EMPTY, FILLED, EXCLUDED = Value
}

class NonogramField private[NonogramField](val body: Array[Array[Cell.Value]]) {

  def getRows: Int = body.length

  def getCols: Int = body(0).length

  def getCell(row: Int, col: Int): Cell.Value = body(row)(col)

}

object NonogramField {
  def apply(rows: Int, cols: Int): NonogramField = {
    val body = Array.tabulate(rows, cols)((i, j) => if (i == j) Cell.FILLED else Cell.EMPTY)
    new NonogramField(body)
  }
}
