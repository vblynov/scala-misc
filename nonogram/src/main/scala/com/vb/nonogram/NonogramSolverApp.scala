package com.vb.nonogram

import com.vb.nonogram.field.BasicNonogramField
import com.vb.nonogram.solver.SequentialNonogramSolver
import com.vb.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = BasicNonogramField("oneStar1.txt")
  val solver: NonogramSolver = new SequentialNonogramSolver
  val solution: NonogramField = solver.solve(data)

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(solution, 20) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
