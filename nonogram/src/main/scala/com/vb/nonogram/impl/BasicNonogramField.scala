package com.vb.nonogram.impl

import com.vb.nonogram.NonogramField

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class BasicNonogramField private[BasicNonogramField](val rows: Int,
                                                     val cols: Int,
                                                     private[BasicNonogramField] val body: ArrayBuffer[Boolean],
                                                     private[BasicNonogramField] val rowGroups: ArrayBuffer[Array[Int]],
                                                     private[BasicNonogramField] val colGroups: ArrayBuffer[Array[Int]]) extends NonogramField {


  override def isFilled(row: Int, col: Int): Boolean = body(row*cols + col)

  override def rowGroup(row: Int): Seq[Int] = rowGroups(row)

  override def colGroup(col: Int): Seq[Int] = colGroups(col)

  override def fillCell(row: Int, col: Int): Boolean = if (body(row*col + col)) false else {
    body(row*col + col) = true
    true
  }

  override def rowCount(): Int = rows

  override def colCount(): Int = cols
}

object BasicNonogramField {
  def apply(fileName: String): BasicNonogramField = {
    val source = Source.fromResource(fileName)
    val lines = source.getLines().toArray
    val rows = lines(0).toInt
    val rowGroup = new ArrayBuffer[Array[Int]](rows)
    for (i <- 0 until rows) {
      rowGroup += lines(i + 1).split(" ").map(_.toInt)
    }
    val cols = lines(rows + 1).toInt
    val colGroup = new ArrayBuffer[Array[Int]](cols)
    for (i <- 0 until cols) {
      colGroup += lines(i + rows + 1).split(" ").map(_.toInt)
    }
    val body = new ArrayBuffer[Boolean](rows * cols)
    for (i <- 0 until rows*cols) {
      body += false
    }
    new BasicNonogramField(rows, cols, body, rowGroup, colGroup)
  }
}
