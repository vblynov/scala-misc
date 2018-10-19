package com.vb.nonogram.impl

import com.vb.nonogram.{NonogramField, NonogramSolver}

class SequentialNonogramSolver extends NonogramSolver {

  def solve(field: NonogramField): NonogramField = {
    val rowPositions = for (i <- (0 until field.rowCount).toArray) yield Position(field.colCount(), field.rowGroup(i))
    val colPositions = for (i <- (0 until field.colCount).toArray) yield Position(field.rowCount(), field.colGroup(i))

    val columnBasedField = new ColumnAccessFieldWrapper(field)

    var changedIndexes: Set[Int] = Set() ++ (0 until field.rowCount)
    while (changedIndexes.nonEmpty) {
      println(changedIndexes)
      // process rows
      changedIndexes = solutionStep(field, field.colCount(), rowPositions, changedIndexes)
      // process cols
      changedIndexes = solutionStep(columnBasedField, field.rowCount(), colPositions, changedIndexes)
    }
    field
  }

  private def solutionStep(field: NonogramField, count: Int, positionGroup: Array[Position], indexes: Set[Int]): Set[Int] = {
    var changedIndexes: Set[Int] = Set[Int]()
    for (currentRow <- indexes if positionGroup(currentRow) != Position.EMPTY_POSITION) {
      val filledCells = for (i <- 0 until count if field.isFilled(currentRow, i)) yield i
      val crossedOutCells = for (i <- 0 until count if field.isCrossedOut(currentRow, i)) yield i

      val currentPosition = positionGroup(currentRow).filterVariants(filledCells, crossedOutCells)
      positionGroup(currentRow) = currentPosition

      val cellsToFill = currentPosition.getIntersection
      val cellsToCrossOut = currentPosition.getDifference
      changedIndexes = changedIndexes ++
        (for (cell <- cellsToFill if field.fillCell(currentRow, cell)) yield cell) ++
        (for (cell <- cellsToCrossOut if field.crossOutCell(currentRow, cell)) yield cell)
      if (currentPosition.variants.length == 1) {
        positionGroup(currentRow) = Position.EMPTY_POSITION
      }
    }
    changedIndexes
  }

}
