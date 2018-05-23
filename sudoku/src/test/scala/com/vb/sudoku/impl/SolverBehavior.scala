package com.vb.sudoku.impl

import org.scalatest.FlatSpec

trait SolverBehavior {this: FlatSpec =>
  def correctSudokuSolver(sudokuSolver: SudokuSolver): Unit = {
    it should "solve correct puzzles" in {
      val field = Field("0 2 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0")
      val expected = "1 2 6 4 3 7 9 5 8 8 9 5 6 2 1 4 7 3 3 7 4 9 8 5 1 2 6 4 5 7 1 9 3 8 6 2 9 8 3 2 4 6 5 1 7 6 1 2 5 7 8 3 9 4 2 6 9 3 1 4 7 8 5 5 4 8 7 6 9 2 3 1 7 3 1 8 5 2 6 4 9"
      val result = sudokuSolver.solve(field)
      assert(result.toString == expected)
    }

    it should "not solve incorrect puzzle" in {
      val field = Field("2 2 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0")
      intercept[IllegalArgumentException] {
        sudokuSolver.solve(field)
      }
    }

    it should "return already solved puzzle" in {
      val expected = "1 2 6 4 3 7 9 5 8 8 9 5 6 2 1 4 7 3 3 7 4 9 8 5 1 2 6 4 5 7 1 9 3 8 6 2 9 8 3 2 4 6 5 1 7 6 1 2 5 7 8 3 9 4 2 6 9 3 1 4 7 8 5 5 4 8 7 6 9 2 3 1 7 3 1 8 5 2 6 4 9"
      val field = Field("1 2 6 4 3 7 9 5 8 8 9 5 6 2 1 4 7 3 3 7 4 9 8 5 1 2 6 4 5 7 1 9 3 8 6 2 9 8 3 2 4 6 5 1 7 6 1 2 5 7 8 3 9 4 2 6 9 3 1 4 7 8 5 5 4 8 7 6 9 2 3 1 7 3 1 8 5 2 6 4 9")
      val result = sudokuSolver.solve(field)
      assert(result.toString == expected)
    }

    it should "solve trivial puzzle" in {
      val expected = "1"
      val field = Field("0")
      val result = sudokuSolver.solve(field)
      assert(result.toString == expected)
    }
  }
}
