package com.vb.sudoku

import com.vb.sudoku.impl.{Field, ParallelSudokuSolver}

object SudokuSolverApp extends App {
  val sudoku = Field("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0")
  val solver = new ParallelSudokuSolver()
  val result = solver.solve(sudoku)
  println(result.toString)
}

