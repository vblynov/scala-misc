package com.vbl.nonogram

import com.vbl.nonogram.field.BasicNonogramField
import com.vbl.nonogram.solver.SequentialNonogramSolver
import com.vbl.nonogram.view.NonogramPanel

import scala.swing.{Dimension, MainFrame, SimpleSwingApplication}

object NonogramSolverApp extends SimpleSwingApplication {

  val data = BasicNonogramField("fiveStar2.txt")
  val solver: NonogramSolver = new SequentialNonogramSolver
  val solution: NonogramField = solver.solve(data)

  def top: MainFrame = new MainFrame {
    contents = new NonogramPanel(solution, 20) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
