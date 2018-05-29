package com.vb.nonogram

import com.vb.nonogram.impl.NonogramField
import com.vb.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = NonogramField(10, 10)

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(data) {
      preferredSize = new Dimension(300, 300)
    }
  }
}
