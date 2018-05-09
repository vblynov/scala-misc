package com.vb.sudoku.domain

case class Cell(row: Int, col: Int, value: Option[Int], availableValues: Array[Int]) {
  val sector: Int = resolveSector

  private def resolveSector: Int = {
    if (row >= 0 && row <= 2 && col >= 0 && col <= 2) 1
    else if (row >= 0 && row <= 2 && col >= 3 && col <= 5) 2
    else if (row >= 0 && row <= 2 && col >= 6 && col <= 8) 3
    else if (row >= 3 && row <= 5 && col >= 0 && col <= 2) 4
    else if (row >= 3 && row <= 5 && col >= 3 && col <= 5) 5
    else if (row >= 3 && row <= 5 && col >= 6 && col <= 8) 6
    else if (row >= 6 && row <= 8 && col >= 0 && col <= 2) 7
    else if (row >= 6 && row <= 8 && col >= 3 && col <= 5) 8
    else 9
  }
}