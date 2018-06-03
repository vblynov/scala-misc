package com.vb.nonogram.impl

import org.scalatest.FlatSpec

class PositionSpec extends FlatSpec {

  it should "should compute correct variants for several elemanets in group" in {
    val postition = Position(10, Array(2, 4, 2))
    val expected = Array(Array(Range(0, 2), Range(3, 7), Range(8, 10)))
    val actual = postition.initialVariants.toArray
    assert(sameVariants(expected, actual))
  }

  it should "should compute correct variants for single elemanets in group" in {
    val postition = Position(10, Array(5))
    val expected = Array(
      Array(Range(0, 5)),
      Array(Range(1, 6)),
      Array(Range(2, 7)),
      Array(Range(3, 8)),
      Array(Range(4, 9)),
      Array(Range(5, 10))
    )
    val actual = postition.initialVariants.toArray
    assert(sameVariants(expected, actual))
  }

  def sameVariants(expected: Array[Array[Range]], actual: Array[Array[Range]]): Boolean =
    (for (i <- expected.indices) yield expected(i) sameElements actual(i)).forall(_ == true)


}
