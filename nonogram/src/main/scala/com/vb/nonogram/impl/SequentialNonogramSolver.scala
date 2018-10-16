package com.vb.nonogram.impl

import com.vb.nonogram.{NonogramField, NonogramSolver}

class SequentialNonogramSolver extends NonogramSolver {

  def solve(field: NonogramField): NonogramField = {
    val rowPositions = for (i <- (0 until field.rowCount).toArray) yield Position(field.colCount(), field.rowGroup(i))
    val colPositions = for (i <- (0 until field.colCount).toArray) yield Position(field.rowCount(), field.colGroup(i))

    var changedIndexes: collection.mutable.Set[Int]= collection.mutable.Set()
    changedIndexes ++= (0 until field.rowCount)
    while (changedIndexes.nonEmpty) {
      // process rows
      println(changedIndexes.size)
      var newChangedIndexes: collection.mutable.Set[Int] = collection.mutable.Set[Int]()
      for (currentRow <- changedIndexes if rowPositions(currentRow) != Position.EMPTY_POSITION) {
        val filledCells = for (i <- 0 until field.colCount if field.isFilled(currentRow, i)) yield i
        val crossedOutCells = for (i <- 0 until field.colCount if field.isCrossedOut(currentRow, i)) yield i

        val currentPosition = rowPositions(currentRow).filterVariants(filledCells, crossedOutCells)
        rowPositions(currentRow) = currentPosition

        val cellsToFill = currentPosition.getIntersection
        val cellsToCrossOut = currentPosition.getDifference
        newChangedIndexes ++= (for (cell <- cellsToFill if field.fillCell(currentRow, cell)) yield cell)
        newChangedIndexes ++= (for (cell <- cellsToCrossOut if field.crossOutCell(currentRow, cell)) yield cell)
        if (currentPosition.variants.length == 1) {
          rowPositions(currentRow) = Position.EMPTY_POSITION
        }

      }
      // process cols
      changedIndexes = newChangedIndexes
      newChangedIndexes = collection.mutable.Set[Int]()
      for (currentCol <- changedIndexes if colPositions(currentCol) != Position.EMPTY_POSITION) {
        val filledCells = for (i <- 0 until field.rowCount if field.isFilled(i, currentCol)) yield i
        val crossedOutCells = for (i <- 0 until field.rowCount if field.isCrossedOut(i, currentCol)) yield i

        val currentPosition = colPositions(currentCol).filterVariants(filledCells, crossedOutCells)
        colPositions(currentCol) = currentPosition
        val cellsToFill = currentPosition.getIntersection
        val cellsToCrossOut = currentPosition.getDifference
        newChangedIndexes ++= (for (cell <- cellsToFill if field.fillCell(cell, currentCol)) yield cell)
        newChangedIndexes ++= (for (cell <- cellsToCrossOut if field.crossOutCell(cell, currentCol)) yield cell)
        if (currentPosition.variants.length == 1) {
          colPositions(currentCol) = Position.EMPTY_POSITION
        }
      }
      changedIndexes = newChangedIndexes
    }
    field
  }

}
