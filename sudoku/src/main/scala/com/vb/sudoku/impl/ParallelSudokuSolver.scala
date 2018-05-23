package com.vb.sudoku.impl

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

class ParallelSudokuSolver extends SudokuSolver {
  override def solve(field: Field): Field = {
    if (field.isWrong) throw new IllegalArgumentException("Field is wrong")
    if (field.isSolved) field
    else {
      val promise = Promise[Field]()
      compute(field, promise)
      Await.result(promise.future, Duration(10000, TimeUnit.SECONDS))
    }
  }

  def compute(field: Field, promise: Promise[Field]): Unit = {
    if (!promise.isCompleted) {
      if (field.isSolved && !field.isWrong) {
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