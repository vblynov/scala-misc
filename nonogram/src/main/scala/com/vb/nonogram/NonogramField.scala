package com.vb.nonogram

import com.vb.nonogram.field.Position

trait NonogramField {

  def rows: Int

  def cols: Int

  def rowGroup(row: Int): Seq[Int]

  def colGroup(col: Int): Seq[Int]

  def isFilled(row: Int, col: Int): Boolean

  def isCrossedOut(row: Int, col: Int): Boolean

  def rowPosition(row: Int): Position

  def colPosition(col: Int): Position

  def isSolved: Boolean

  def isWrong: Boolean

  def applyRowVariant(row: Int, variant: Seq[Int]): NonogramField

  def applyColVariant(col: Int, variant: Seq[Int]): NonogramField

}
