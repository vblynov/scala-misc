package com.vb.nonogram.impl

import org.scalatest.FlatSpec

class PositionSpec extends FlatSpec {

  it should "should compute correct variants for several elements in group" in {
    val postition = Position(10, Array(2, 4, 2))
    val expected = Array(Array(0, 1, 3, 4, 5, 6, 8, 9))
    val actual = postition.variants
    assert(sameVariants(expected, actual))
  }

  it should "should compute correct variants for single elemanets in group" in {
    val postition = Position(10, Array(5))
    val expected = Array(
      Array(0, 1, 2, 3, 4),
      Array(1, 2, 3, 4, 5),
      Array(2, 3, 4, 5, 6),
      Array(3, 4, 5, 6, 7),
      Array(4, 5, 6, 7, 8),
      Array(5, 6, 7, 8, 9)
    )
    val actual = postition.variants
    assert(sameVariants(expected, actual))
  }

  def sameVariants(expected: Array[Array[Int]], actual: Array[Array[Int]]): Boolean =
    (for (i <- expected.indices) yield expected(i) sameElements actual(i)).forall(_ == true)


}
