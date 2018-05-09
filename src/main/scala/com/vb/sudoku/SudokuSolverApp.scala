package com.vb.sudoku

import com.vb.sudoku.domain.Field

object SudokuSolverApp extends App {
  val sudoku = Field("0 2 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0")
  val solver = new SequentialSudokuSolver()
  val result = solver.solve(sudoku)
  result.print()
}

