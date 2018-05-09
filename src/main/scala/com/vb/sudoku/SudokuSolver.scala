package com.vb.sudoku

import com.vb.sudoku.domain.Field

trait SudokuSolver {
  def solve(sudoku: Field): Field
}
