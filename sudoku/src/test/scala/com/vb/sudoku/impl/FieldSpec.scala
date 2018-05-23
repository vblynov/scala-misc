package com.vb.sudoku.impl

import org.scalatest.FlatSpec

class FieldSpec extends FlatSpec {

  it should "not create field of incorrect size" in {
    intercept[IllegalArgumentException] {
      Field("2 2 0 0")
    }
  }

  it should "not create field of 0 size" in {
    intercept[IllegalArgumentException] {
      Field(0)
    }
  }

  it should "not create field from empty string" in {
    intercept[IllegalArgumentException] {
      Field("")
    }
  }

  it should "detect wrong field (incorrect rules)" in {
    val field = Field("2 2 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0")
    assert(field.isWrong)
  }

  it should "detect wrong field (incorrect values)" in {
    val field = Field("2 10 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0")
    assert(field.isWrong)
  }

  it should "detect solved field" in {
    val field = Field("1 2 6 4 3 7 9 5 8 8 9 5 6 2 1 4 7 3 3 7 4 9 8 5 1 2 6 4 5 7 1 9 3 8 6 2 9 8 3 2 4 6 5 1 7 6 1 2 5 7 8 3 9 4 2 6 9 3 1 4 7 8 5 5 4 8 7 6 9 2 3 1 7 3 1 8 5 2 6 4 9")
    assert(field.isSolved)
  }

  it should "create (solved) field from array" in {
    val field = Field(Array(1,2,6,4,3,7,9,5,8,8,9,5,6,2,1,4,7,3,3,7,4,9,8,5,1,2,6,4,5,7,1,9,3,8,6,2,9,8,3,2,4,6,5,1,7,6,1,2,5,7,8,3,9,4,2,6,9,3,1,4,7,8,5,5,4,8,7,6,9,2,3,1,7,3,1,8,5,2,6,4,9))
    assert(field.isSolved)
  }
}
