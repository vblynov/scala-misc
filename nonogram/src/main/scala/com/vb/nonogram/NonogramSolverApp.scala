package com.vb.nonogram

import com.vb.nonogram.impl.NonogramField
import com.vb.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = NonogramField(35, 30)

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(data) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
