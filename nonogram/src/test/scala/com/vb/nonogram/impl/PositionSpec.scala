package com.vb.nonogram.impl

import org.scalatest.FlatSpec

import scala.collection.immutable.BitSet

class PositionSpec extends FlatSpec {

  it should "should compute correct variants for several elements in group" in {
    val postition = Position(10, Array(2, 4, 2))
    val expected = Array(BitSet(0, 1, 3, 4, 5, 6, 8, 9))
    val actual = postition.variants
    assert(sameVariants(expected, actual))
  }

  it should "should compute correct variants for single elemanets in group" in {
    val postition = Position(10, Array(5))
    val expected = Array(
      BitSet(0, 1, 2, 3, 4),
      BitSet(1, 2, 3, 4, 5),
      BitSet(2, 3, 4, 5, 6),
      BitSet(3, 4, 5, 6, 7),
      BitSet(4, 5, 6, 7, 8),
      BitSet(5, 6, 7, 8, 9)
    )
    val actual = postition.variants
    assert(sameVariants(expected, actual))
  }

  def sameVariants(expected: Array[BitSet], actual: Array[BitSet]): Boolean =
    (for (i <- expected.indices) yield expected(i) sameElements actual(i)).forall(_ == true)


}
