package com.vb.sudoku.impl

class SequentialSudokuSolver extends SudokuSolver {
  override def solve(field: Field): Field = {
    if (field.isWrong) throw new IllegalArgumentException("Field is wrong")
    if (field.isSolved) field
    else doSolve(field)
  }

  private def doSolve(field: Field): Field = {
    if (field.isWrong || field.isSolved) {
      field
    } else {
      val nextCell = field.getMinCell
      for (value <- nextCell.availableValues) {
        val f = doSolve(field.setValue(nextCell.row, nextCell.col, value))
        if (f.isSolved) return f
      }
      field
    }
  }

}
