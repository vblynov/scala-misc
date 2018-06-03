package com.vb.nonogram

import com.vb.nonogram.impl.NonogramField
import com.vb.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = NonogramField("small1.txt")

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(data, 20) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
