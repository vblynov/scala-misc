package com.vb.nonogram.impl

import com.vb.nonogram.NonogramField

class ColumnAccessFieldWrapper(field: NonogramField) extends NonogramField {

  override def rowCount(): Int = field.rowCount()

  override def colCount(): Int = field.colCount()

  override def isFilled(row: Int, col: Int): Boolean = field.isFilled(col, row)

  override def isCrossedOut(row: Int, col: Int): Boolean = field.isCrossedOut(col, row)

  override def rowGroup(row: Int): Seq[Int] = field.rowGroup(row)

  override def colGroup(col: Int): Seq[Int] = field.colGroup(col)

  override def fillCell(row: Int, col: Int): Boolean = field.fillCell(col, row)

  override def crossOutCell(row: Int, col: Int): Boolean = field.crossOutCell(col, row)
}
