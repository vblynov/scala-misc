package com.vb.nonogram

import com.vb.nonogram.impl.{BasicNonogramField, SequentialNonogramSolver}
import com.vb.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = BasicNonogramField("oneStar4.txt")
  val solver: NonogramSolver = new SequentialNonogramSolver
  val solution: NonogramField = solver.solve(data)

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(solution, 20) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
