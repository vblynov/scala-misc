package com.vb.sudoku.impl

import org.scalatest.FlatSpec

class SudokuSolverSpec extends FlatSpec with SolverBehavior {

  def sequentialSolver = new SequentialSudokuSolver
  def parallelSolver = new ParallelSudokuSolver

  "Sequential solver" should behave like correctSudokuSolver(sequentialSolver)
  "Parallel solver" should behave like correctSudokuSolver(parallelSolver)

}
