package com.vb.nonogram.view
import java.awt.geom.Line2D
import java.awt.{BasicStroke, Color, Graphics2D}

import com.vb.nonogram.impl.{Cell, NonogramField}

import scala.swing.Panel

class NonogramPanel(field: NonogramField) extends Panel {

  override def paintComponent(canvas: Graphics2D) {
    val squareSize = 20//canvas.getClipBounds.width.toFloat / field.getRows

    val x0 = 200
    val y0 = 200

    // horizontal lines
    for {i <- 0 to field.getRows} {
      canvas.setStroke(new BasicStroke())
      canvas.setColor(Color.BLACK)
      canvas.draw(new Line2D.Double(x0, y0 + i * squareSize, x0 + field.getCols * squareSize ,y0 + i * squareSize))
    }

    //vertical lines
    for {i <- 0 to field.getCols} {
      canvas.setStroke(new BasicStroke())
      canvas.setColor(Color.BLACK)
      canvas.draw(new Line2D.Double(x0 + i * squareSize, y0, x0 + i * squareSize, y0 + field.getRows * squareSize))
    }

    //row groups
    for (i <- 0 until field.getRows) {
      val group = field.getRowGroup(i).reverse
      canvas.setColor(Color.BLACK)
      for (j <- group.indices) {
        canvas.drawString(group(j).toString, (x0 - 8 - squareSize/2) - j * squareSize, (y0 + 4 + squareSize/2) + i * squareSize)
      }
    }

    //col groups
    for (i <- 0 until field.getCols) {
      val group = field.getColGroup(i).reverse
      canvas.setColor(Color.BLACK)
      for (j <- group.indices) {
        canvas.drawString(group(j).toString, x0 + i * squareSize, (y0 - squareSize/2) - j * squareSize)
      }
    }

    // fill squares
    for {
      i <- 0 until field.getRows
      j <- 0 until field.getCols
      x1 = x0 + j * squareSize
      y1 = y0 + i * squareSize
    } {
      field.getCell(i, j) match {
        case Cell.FILLED => canvas.setColor(Color.BLACK)
        case Cell.EMPTY => canvas.setColor(Color.WHITE)
        case Cell.EXCLUDED => canvas.setColor(Color.GRAY)
      }
      canvas.fillRect(x1 + 1, y1 + 1, squareSize - 1, squareSize - 1)
    }
  }
}
