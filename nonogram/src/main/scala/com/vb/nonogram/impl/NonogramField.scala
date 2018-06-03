package com.vb.nonogram.impl

import scala.io.Source
import scala.util.Random

class NonogramField private[NonogramField](private val body: Array[Array[Cell.Value]],
                                           private val rowGroups: Array[Array[Int]],
                                           private val colGroups: Array[Array[Int]]) {

  private val rowPositions: Array[Position] = rowGroups.map(group => Position(group.length, group))
  private val colPositions: Array[Position] = colGroups.map(group => Position(group.length, group))

  def rows: Int = body.length

  def cols: Int = body(0).length

  def cell(row: Int, col: Int): Cell.Value = body(row)(col)

  def rowGroup(row: Int): Array[Int] = rowGroups(row)

  def colGroup(col: Int): Array[Int] = colGroups(col)

}

object NonogramField {
  def apply(rows: Int, cols: Int): NonogramField = {
    val body = Array.tabulate(rows, cols)((i, j) => if (i == j) Cell.FILLED else Cell.EMPTY)

    val colGroups = for (i <- Array.range(0, cols)) yield {
      val size = Random.nextInt(8) + 1
      for (i <- Array.range(0, size)) yield Random.nextInt(cols - 2) + 1
    }

    val rowGroups = for (i <- Array.range(0, rows)) yield {
      val size = Random.nextInt(8) + 1
      for (i <- Array.range(0, size)) yield Random.nextInt(rows - 2) + 1
    }

    new NonogramField(body, rowGroups, colGroups)
  }

  def apply(fileName: String): NonogramField = {
    val source = Source.fromResource(fileName)
    val lines = source.getLines().toArray
    val rowGroup = for (i <- Array.range(1, lines(0).toInt + 1)) yield lines(i).split(" ").map(_.toInt)
    val colGroup = for (i <- Array.range(rowGroup.length + 2, lines.length)) yield lines(i).split(" ").map(_.toInt)
    val body = Array.tabulate(rowGroup.length, colGroup.length)((i, j) => Cell.EMPTY)
    new NonogramField(body, rowGroup, colGroup)
  }
}
