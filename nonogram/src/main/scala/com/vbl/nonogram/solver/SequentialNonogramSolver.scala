package com.vbl.nonogram.solver

import com.vbl.nonogram.{NonogramField, NonogramSolver}
import com.vbl.nonogram.field.Position

class SequentialNonogramSolver extends NonogramSolver {

  def solve(field: NonogramField): NonogramField = {
    if (!field.isWrong && !field.isSolved) {
      doSolve(field)
    } else field
  }

  def doSolve(field: NonogramField): NonogramField = {
    if (field.isSolved || field.isWrong) {
      field
    } else {
      val pendingRowPositions = for (i <- 0 until field.rows if !(field.rowPosition(i) eq Position.EMPTY_POSITION)) yield (field.rowPosition(i), i, true)
      val pendingColPositions = for (i <- 0 until field.cols if !(field.colPosition(i) eq Position.EMPTY_POSITION)) yield (field.colPosition(i), i, false)
      val mergedPositions = pendingRowPositions ++ pendingColPositions
      if (mergedPositions.nonEmpty) {
        val (position, row, isRow) = mergedPositions.minBy(_._1.getVariants.size)
        for (variant <- position.getVariants) {
          val f = if (isRow) solve(field.applyRowVariant(row, variant)) else solve(field.applyColVariant(row, variant))
          if (f.isSolved) return f
        }
        field
      } else {
        field
      }
    }
  }

}
