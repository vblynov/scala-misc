package com.vb.sudoku.domain

import org.scalatest.FunSuite

class CellSuite extends FunSuite {

  test("should determine correct sector by position ") {
    var cell = Cell(0, 0, None, Array(1, 2, 3))
    assert(cell.sector == 1)

    cell = Cell(0, 3, None, Array(1, 2, 3))
    assert(cell.sector == 2)

    cell = Cell(0, 6, None, Array(1, 2, 3))
    assert(cell.sector == 3)

    cell = Cell(3, 0, None, Array(1, 2, 3))
    assert(cell.sector == 4)

    cell = Cell(3, 3, None, Array(1, 2, 3))
    assert(cell.sector == 5)

    cell = Cell(3, 6, None, Array(1, 2, 3))
    assert(cell.sector == 6)

    cell = Cell(8, 2, None, Array(1, 2, 3))
    assert(cell.sector == 7)

    cell = Cell(8, 5, None, Array(1, 2, 3))
    assert(cell.sector == 8)

    cell = Cell(8, 8, None, Array(1, 2, 3))
    assert(cell.sector == 9)

  }


}
