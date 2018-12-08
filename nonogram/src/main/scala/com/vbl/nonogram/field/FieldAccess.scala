package com.vbl.nonogram.field

private trait FieldAccess {
  def position(positionIndex: Int): Position

  def isFilled(row: Int, col: Int): Boolean

  def isCrossedOut(row: Int, col: Int): Boolean

  def fillCell(row: Int, col: Int): Boolean

  def crossOutCell(row: Int, col: Int): Boolean

  def setPosition(positionIndex: Int, position: Position)

  def count: Int
}
