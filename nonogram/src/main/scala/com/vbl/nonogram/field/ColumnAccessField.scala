package com.vbl.nonogram.field

private class ColumnAccessField(field: BasicNonogramField) extends FieldAccess {
  override def position(positionIndex: Int): Position = field.colPosition(positionIndex)

  override def isFilled(row: Int, col: Int): Boolean = field.isFilled(col, row)

  override def isCrossedOut(row: Int, col: Int): Boolean = field.isCrossedOut(col, row)

  override def fillCell(row: Int, col: Int): Boolean = field.fillCell(col, row)

  override def crossOutCell(row: Int, col: Int): Boolean = field.crossOutCell(col, row)

  override def count: Int = field.rows

  override def setPosition(positionIndex: Int, position: Position): Unit = field.setColPosition(positionIndex, position)
}
