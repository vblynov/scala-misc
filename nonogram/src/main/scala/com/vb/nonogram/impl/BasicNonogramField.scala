package com.vb.nonogram.impl

import com.vb.nonogram.NonogramField

import scala.io.Source

class BasicNonogramField private[BasicNonogramField](private[BasicNonogramField] val rows: Int,
                                                     private[BasicNonogramField] val cols: Int,
                                                     private[BasicNonogramField] val body: Array[Byte],
                                                     private[BasicNonogramField] val rowGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val colGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val rowPositions: Array[Position],
                                                     private[BasicNonogramField] val colPositions: Array[Position]) extends NonogramField {

  override def rowGroup(row: Int): Seq[Int] = rowGroups(row)

  override def colGroup(col: Int): Seq[Int] = colGroups(col)

  override def rowCount(): Int = rows

  override def colCount(): Int = cols

  override def rowPosition(row: Int): Position = rowPositions(row)

  override def colPosition(col: Int): Position = colPositions(col)

  override def isFilled(row: Int, col: Int): Boolean = body(row*cols + col) == 1

  override def isCrossedOut(row: Int, col: Int): Boolean = body(row*cols + col) == 2

  override def converge(): NonogramField = {
    var changedIndexes: Set[Int] = Set() ++ (0 until rows)
    changedIndexes = convergeRows(changedIndexes)
    if (changedIndexes.isEmpty) {
      changedIndexes = Set() ++ (0 until cols)
    }
    changedIndexes = convergeCols(changedIndexes)
    while (changedIndexes.nonEmpty) {
      // process rows
      changedIndexes = convergeRows(changedIndexes)
      // process cols
      changedIndexes = convergeCols(changedIndexes)
    }
    this
  }

  override def isSolved: Boolean = {
    lazy val rowPositionsDone = rowPositions.forall(_ eq Position.EMPTY_POSITION)
    lazy val colPositionsDone = colPositions.forall(_ eq Position.EMPTY_POSITION)
    lazy val bodySolved = !body.contains(0)
    rowPositionsDone && colPositionsDone && bodySolved
  }

  override def isWrong: Boolean = {
    val rowPositionsWrong = rowPositions.forall(pos => (pos eq Position.EMPTY_POSITION) || pos.getVariants.nonEmpty)
    val colPositionsWrong = colPositions.forall(pos => (pos eq Position.EMPTY_POSITION) || pos.getVariants.nonEmpty)

    !rowPositionsWrong || !colPositionsWrong
  }

  override def applyRowVariant(row: Int, variant: Seq[Int]): NonogramField = {
    val copyField = copy
    for (i <- variant) copyField.fillCell(row, i)
    for (i <- 0 until cols) copyField.crossOutCell(row, i)
    copyField.rowPositions(row) = Position.EMPTY_POSITION
    copyField
  }

  override def applyColVariant(col: Int, variant: Seq[Int]): NonogramField = {
    val copyField = copy
    for (i <- variant) copyField.fillCell(col, i)
    for (i <- 0 until rows) copyField.crossOutCell(col, i)
    copyField.colPositions(col) = Position.EMPTY_POSITION
    copyField
  }

  private def fillCell(row: Int, col: Int): Boolean = if (body(row*cols + col) == 1) false else {
    body(row*cols + col) = 1
    true
  }

  private def crossOutCell(row: Int, col: Int): Boolean = if (body(row*cols + col) != 0) false else {
    body(row*cols + col) = 2
    true
  }

  private def convergeRows(indexes: Set[Int]): Set[Int] = {
    var changedIndexes: Set[Int] = Set[Int]()
    for (currentRow <- indexes if !(rowPositions(currentRow) eq Position.EMPTY_POSITION)) {
      val filledCells = for (i <- 0 until cols if isFilled(currentRow, i)) yield i
      val crossedOutCells = for (i <- 0 until cols if isCrossedOut(currentRow, i)) yield i
      val currentPosition = rowPositions(currentRow).filterVariants(filledCells, crossedOutCells)
      rowPositions(currentRow) = currentPosition
      if (currentPosition.getVariantsCount == 1) {
        val variant = currentPosition.getVariants.head
        changedIndexes = changedIndexes ++
          (for (i <- variant if fillCell(currentRow, i)) yield i) ++
          (for (i <- 0 until cols if crossOutCell(currentRow, i)) yield i)
        rowPositions(currentRow) = Position.EMPTY_POSITION
      } else {
        val cellsToFill = currentPosition.getIntersection
        val cellsToCrossOut = currentPosition.getDifference
        changedIndexes = changedIndexes ++
          (for (cell <- cellsToFill if fillCell(currentRow, cell)) yield cell) ++
          (for (cell <- cellsToCrossOut if crossOutCell(currentRow, cell)) yield cell)
      }
    }
    changedIndexes
  }

  private def convergeCols(indexes: Set[Int]): Set[Int] = {
    var changedIndexes: Set[Int] = Set[Int]()
    for (currentCol <- indexes if colPositions(currentCol) != Position.EMPTY_POSITION) {
      val filledCells = for (i <- 0 until rows if isFilled(i, currentCol)) yield i
      val crossedOutCells = for (i <- 0 until rows if isCrossedOut(i, currentCol)) yield i

      val currentPosition = colPositions(currentCol).filterVariants(filledCells, crossedOutCells)
      colPositions(currentCol) = currentPosition
      if (currentPosition.getVariantsCount == 1) {
        val variant = currentPosition.getVariants.head
        changedIndexes = changedIndexes ++
          (for (i <- variant if fillCell(i, currentCol)) yield i) ++
          (for (i <- 0 until rows if crossOutCell(i, currentCol)) yield i)
        colPositions(currentCol) = Position.EMPTY_POSITION
      } else {
        val cellsToFill = currentPosition.getIntersection
        val cellsToCrossOut = currentPosition.getDifference
        changedIndexes = changedIndexes ++
          (for (cell <- cellsToFill if fillCell(cell, currentCol)) yield cell) ++
          (for (cell <- cellsToCrossOut if crossOutCell(cell, currentCol)) yield cell)
      }
    }
    changedIndexes
  }

  private def copy: BasicNonogramField = {
      val newBody = for (i <- body.indices.toArray) yield body(i)
      val newRowPositions = for (i <- rowPositions.indices.toArray) yield rowPositions(i)
      val newColPositions = for (i <- colPositions.indices.toArray) yield colPositions(i)
      new BasicNonogramField(rows, cols, newBody, rowGroups, colGroups, newRowPositions, newColPositions)
  }

}

object BasicNonogramField {
  def apply(fileName: String): BasicNonogramField = {
    val source = Source.fromResource(fileName)
    val lines = source.getLines().toArray
    val rows = lines(0).toInt
    val rowGroup = lines.tail.map(_.split(" "))
      .map(_.map(_.toInt).toVector)
      .toVector
    val cols = lines(rows + 1).toInt
    val colGroup = lines.takeRight(lines.length - rows - 2)
      .map(_.split(" "))
      .map(_.map(_.toInt).toVector)
      .toVector
    val body = Array.fill[Byte](rows * cols)(0)
    val rowPositions = for (i <- (0 until rows).toArray) yield Position(cols, rowGroup(i))
    val colPositions = for (i <- (0 until cols).toArray) yield Position(rows, colGroup(i))

    new BasicNonogramField(rows, cols, body, rowGroup, colGroup, rowPositions, colPositions)
  }
}
