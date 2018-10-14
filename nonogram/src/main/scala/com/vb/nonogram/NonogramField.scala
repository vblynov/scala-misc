package com.vb.nonogram

trait NonogramField {

  def rowCount(): Int

  def colCount(): Int

  def isFilled(row: Int, col: Int): Boolean

  def rowGroup(row: Int): Seq[Int]

  def colGroup(col: Int): Seq[Int]

  def fillCell(row: Int, col: Int): Boolean
}
