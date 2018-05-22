package com.vb.sudoku

class SequentialSudokuSolver extends SudokuSolver {
  override def solve(sudoku: Field): Field = {
    if (sudoku.isWrong || sudoku.isSolved) {
      sudoku
    } else {
      val nextCell = sudoku.getMinCell
      for (value <- nextCell.availableValues) {
        val f = solve(sudoku.setValue(nextCell.row, nextCell.col, value))
        if (f.isSolved) return f
      }
      sudoku
    }
  }
}