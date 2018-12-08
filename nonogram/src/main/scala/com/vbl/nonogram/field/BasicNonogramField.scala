package com.vbl.nonogram.field

import com.vbl.nonogram.NonogramField

import scala.io.Source

class BasicNonogramField private[BasicNonogramField](val rows: Int,
                                                     val cols: Int,
                                                     private[BasicNonogramField] val body: Array[Byte],
                                                     private[BasicNonogramField] val rowGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val colGroups: IndexedSeq[IndexedSeq[Int]],
                                                     private[BasicNonogramField] val rowPositions: Array[Position],
                                                     private[BasicNonogramField] val colPositions: Array[Position]) extends NonogramField {

  private[BasicNonogramField] val rowAccessField = new RowAccessField(this)
  private[BasicNonogramField] val columnAccessField = new ColumnAccessField(this)
  private[this] val allRows: Set[Int] = (0 until rows).toSet
  private[this] val allCols: Set[Int] = (0 until cols).toSet

  override def rowGroup(row: Int): Seq[Int] = rowGroups(row)

  override def colGroup(col: Int): Seq[Int] = colGroups(col)

  override def rowPosition(row: Int): Position = rowPositions(row)

  override def colPosition(col: Int): Position = colPositions(col)

  override def isFilled(row: Int, col: Int): Boolean = body(index(row, col)) == 1

  override def isCrossedOut(row: Int, col: Int): Boolean = body(index(row, col)) == 2

  override def isSolved: Boolean = {
    lazy val rowPositionsDone = rowPositions.forall(_ eq Position.EMPTY_POSITION)
    lazy val colPositionsDone = colPositions.forall(_ eq Position.EMPTY_POSITION)
    lazy val bodySolved = !body.contains(0)
    rowPositionsDone && colPositionsDone && bodySolved
  }

  override def isWrong: Boolean = {
    val rowPositionsRight = rowPositions.forall(pos => (pos eq Position.EMPTY_POSITION) || pos.getVariants.nonEmpty)
    val colPositionsRight = colPositions.forall(pos => (pos eq Position.EMPTY_POSITION) || pos.getVariants.nonEmpty)

    !(rowPositionsRight && colPositionsRight)
  }

  override def applyRowVariant(row: Int, variant: Seq[Int]): NonogramField = {
    val copyField = copy
    applyVariant(copyField, copyField.rowAccessField, row, variant)
  }

  override def applyColVariant(col: Int, variant: Seq[Int]): NonogramField = {
    val copyField = copy
    applyVariant(copyField, copyField.columnAccessField, col, variant)
  }

  private[field] def fillCell(row: Int, col: Int): Boolean = if (body(index(row, col)) == 1) false else {
    body(index(row, col)) = 1
    true
  }

  private[field] def crossOutCell(row: Int, col: Int): Boolean = if (body(index(row, col)) != 0) false else {
    body(index(row, col)) = 2
    true
  }

  private[field] def setRowPosition(positionIndex: Int, position: Position): Unit = {
    rowPositions(positionIndex) = position
  }

  private[field] def setColPosition(positionIndex: Int, position: Position): Unit = {
    colPositions(positionIndex) = position
  }

  private[BasicNonogramField] def converge(): Unit = {
    Stream.iterate(allRows)(indexes => {
      val changedRows = convergeDimension(rowAccessField, indexes)
      if (changedRows.isEmpty) convergeDimension(columnAccessField, allCols)
      else convergeDimension(columnAccessField, changedRows)
    }).dropWhile(_.nonEmpty)
  }

  private[this] def index(row: Int, col: Int): Int = row * cols + col

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

  private[this] def applyVariant(field: BasicNonogramField, access: FieldAccess, index: Int, variant: Seq[Int]): NonogramField = {
    for (i <- variant) access.fillCell(index, i)
    for (i <- 0 until access.count) access.crossOutCell(index, i)
    access.setPosition(index, Position.EMPTY_POSITION)
    field.converge()
    field
  }
}

object BasicNonogramField {
  def apply(fileName: String): BasicNonogramField = {
    val source = Source.fromResource(fileName)
    val lines = source.getLines().toArray
    val rows = lines(0).toInt
    val rowGroup = lines.tail.take(rows).map(_.split(" "))
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

    val resultField = new BasicNonogramField(rows, cols, body, rowGroup, colGroup, rowPositions, colPositions)
    resultField.converge()
    resultField
  }
}
