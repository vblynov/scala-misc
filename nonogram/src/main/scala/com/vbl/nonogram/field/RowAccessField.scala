package com.vbl.nonogram.field

private class RowAccessField(field: BasicNonogramField) extends FieldAccess {
  override def position(positionIndex: Int): Position = field.rowPosition(positionIndex)

  override def isFilled(row: Int, col: Int): Boolean = field.isFilled(row, col)

  override def isCrossedOut(row: Int, col: Int): Boolean = field.isCrossedOut(row, col)

  override def fillCell(row: Int, col: Int): Boolean = field.fillCell(row, col)

  override def crossOutCell(row: Int, col: Int): Boolean = field.crossOutCell(row, col)

  override def count: Int = field.cols

  override def setPosition(positionIndex: Int, position: Position): Unit = field.setRowPosition(positionIndex, position)
}
