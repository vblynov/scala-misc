package com.vb.nonogram.field

import com.vb.nonogram.NonogramField

import scala.io.Source

class BasicNonogramField private[BasicNonogramField](private[BasicNonogramField] val rows: Int,
                                                     private[BasicNonogramField] val cols: Int,
                                                     private[BasicNonogramField] val body: Array[Byte],
                                                     private[BasicNonogramField] val rowGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val colGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val rowPositions: Array[Position],
                                                     private[BasicNonogramField] val colPositions: Array[Position]) extends NonogramField {

  private[this] val rowAccessField = new RowAccessField(this)
  private[this] val columnAccessField = new ColumnAccessField(this)

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
    changedIndexes = convergeDimension(rowAccessField, changedIndexes)
    if (changedIndexes.isEmpty) {
      changedIndexes = Set() ++ (0 until cols)
    }
    changedIndexes = convergeDimension(columnAccessField, changedIndexes)
    while (changedIndexes.nonEmpty) {
      // process rows
      changedIndexes = convergeDimension(rowAccessField, changedIndexes)
      // process cols
      changedIndexes = convergeDimension(columnAccessField, changedIndexes)
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

  private[field] def fillCell(row: Int, col: Int): Boolean = if (body(row*cols + col) == 1) false else {
    body(row*cols + col) = 1
    true
  }

  private[field] def crossOutCell(row: Int, col: Int): Boolean = if (body(row*cols + col) != 0) false else {
    body(row*cols + col) = 2
    true
  }

  private[field] def setRowPosition(positionIndex: Int, position: Position): Unit = {
    rowPositions(positionIndex) = position
  }

  private[field] def setColPosition(positionIndex: Int, position: Position): Unit = {
    colPositions(positionIndex) = position
  }

  private[this] def convergeDimension(field: FieldAccess, indexes: Set[Int]): Set[Int] = {
    var changedIndexes: Set[Int] = Set[Int]()
    for (currentIndex <- indexes if !(field.position(currentIndex) eq Position.EMPTY_POSITION)) {
      val filledCells = for (i <- 0 until field.count if field.isFilled(currentIndex, i)) yield i
      val crossedOutCells = for (i <- 0 until field.count if field.isCrossedOut(currentIndex, i)) yield i
      val currentPosition = field.position(currentIndex).filterVariants(filledCells, crossedOutCells)
      field.setPosition(currentIndex, currentPosition)
      if (currentPosition.getVariantsCount == 1) {
        val variant = currentPosition.getVariants.head
        changedIndexes = changedIndexes ++
          (for (i <- variant if field.fillCell(currentIndex, i)) yield i) ++
          (for (i <- 0 until cols if field.crossOutCell(currentIndex, i)) yield i)
        field.setPosition(currentIndex, Position.EMPTY_POSITION)
      } else {
        val cellsToFill = currentPosition.getIntersection
        val cellsToCrossOut = currentPosition.getDifference
        changedIndexes = changedIndexes ++
          (for (cell <- cellsToFill if field.fillCell(currentIndex, cell)) yield cell) ++
          (for (cell <- cellsToCrossOut if field.crossOutCell(currentIndex, cell)) yield cell)
      }
    }
    changedIndexes
  }

  private[this] def copy: BasicNonogramField = {
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
