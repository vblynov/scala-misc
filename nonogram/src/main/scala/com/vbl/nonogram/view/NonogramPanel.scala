package com.vbl.nonogram.view

import java.awt.geom.Line2D
import java.awt.{BasicStroke, Color, Graphics2D}

import com.vbl.nonogram.NonogramField

import scala.swing.Panel

class NonogramPanel(val field: NonogramField, val squareSize: Int) extends Panel {

  override def paintComponent(canvas: Graphics2D) {
    val canvasWidth = canvas.getClipBounds.width
    val canvasHeight = canvas.getClipBounds.height

    val maxRowGroupSize = (for (i <- 0 until field.rows) yield field.rowGroup(i).length).max
    val maxColGroupSize = (for (i <- 0 until field.cols) yield field.colGroup(i).length).max

    val x0 = 10 + maxRowGroupSize * squareSize
    val y0 = 10 + maxColGroupSize * squareSize

    // horizontal lines
    for {i <- 0 to field.rows} {
      canvas.setStroke(new BasicStroke())
      canvas.setColor(Color.BLACK)
      canvas.draw(new Line2D.Double(x0, y0 + i * squareSize, x0 + field.cols * squareSize, y0 + i * squareSize))
    }

    //vertical lines
    for {i <- 0 to field.cols} {
      canvas.setStroke(new BasicStroke())
      canvas.setColor(Color.BLACK)
      canvas.draw(new Line2D.Double(x0 + i * squareSize, y0, x0 + i * squareSize, y0 + field.rows * squareSize))
    }

    //row groups
    for (i <- 0 until field.rows) {
      val group = field.rowGroup(i).reverse
      canvas.setColor(Color.BLACK)
      for (j <- group.indices) {
        canvas.drawString(group(j).toString, (x0 - 8 - squareSize/2) - j * squareSize, (y0 + 4 + squareSize/2) + i * squareSize)
      }
    }

    //col groups
    for (i <- 0 until field.cols) {
      val group = field.colGroup(i).reverse
      canvas.setColor(Color.BLACK)
      for (j <- group.indices) {
        canvas.drawString(group(j).toString, x0 + i * squareSize, (y0 - squareSize/2) - j * squareSize)
      }
    }

    // fill squares
    for {
      i <- 0 until field.rows
      j <- 0 until field.cols
      x1 = x0 + j * squareSize
      y1 = y0 + i * squareSize
    } {
      if (field.isFilled(i, j)) {
        canvas.setColor(Color.BLACK)
      } else if (field.isCrossedOut(i, j)) {
        canvas.setColor(Color.GRAY)
      } else {
        canvas.setColor(Color.WHITE)
      }
      canvas.fillRect(x1 + 1, y1 + 1, squareSize - 1, squareSize - 1)
    }
  }

}
