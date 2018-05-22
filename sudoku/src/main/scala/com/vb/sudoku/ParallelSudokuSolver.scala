package com.vb.sudoku

import java.util.concurrent.TimeUnit

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ParallelSudokuSolver extends SudokuSolver {
  override def solve(sudoku: Field): Field = {
    val promise = Promise[Field]()
    compute(sudoku, promise)
    Await.result(promise.future, Duration(10000, TimeUnit.SECONDS))
  }

  def compute(field: Field, promise: Promise[Field]): Unit = {
    if (!promise.isCompleted) {
      if (field.isSolved) {
        promise.success(field)
      } else if (!field.isWrong) {
        val nextCell = field.getMinCell
        if (nextCell.availableValues.length == 1) {
          compute(field.setValue(nextCell.row, nextCell.col, nextCell.availableValues(0)), promise)
        } else {
          nextCell.availableValues.foreach(v => Future {
            compute(field.setValue(nextCell.row, nextCell.col, v), promise)
          })
        }
      }
    }
  }

}