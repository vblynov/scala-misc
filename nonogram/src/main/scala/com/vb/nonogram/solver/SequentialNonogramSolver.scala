package com.vb.nonogram.solver

import com.vb.nonogram.field.Position
import com.vb.nonogram.{NonogramField, NonogramSolver}

class SequentialNonogramSolver extends NonogramSolver {

  def solve(field: NonogramField): NonogramField = {
    if (!field.isWrong && !field.isSolved) {
      doSolve(field.converge())
    } else field
  }

  def doSolve(field: NonogramField): NonogramField = {
    if (field.isSolved || field.isWrong) {
      field
    } else {
      val pendingRowPositions = for (i <- 0 until field.rowCount if field.rowPosition(i) != Position.EMPTY_POSITION) yield (field.rowPosition(i), i, true)
      val pendingColPositions = for (i <- 0 until field.colCount if field.colPosition(i) != Position.EMPTY_POSITION) yield (field.colPosition(i), i, false)
      val mergedPositions = pendingRowPositions ++ pendingColPositions
      if (mergedPositions.nonEmpty) {
        val (position, row, isRow) = mergedPositions.minBy(_._1.getVariants.size)
        if (isRow) {
          for (varinat <- position.getVariants) {
            val f = solve(field.applyRowVariant(row, varinat).converge())
            if (f.isSolved) return f
          }
          field
        } else {
          for (varinat <- position.getVariants) {
            val f = solve(field.applyColVariant(row, varinat).converge())
            if (f.isSolved) return f
          }
          field
        }
      } else {
        field
      }
    }
  }

}
