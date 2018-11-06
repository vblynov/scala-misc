package com.vb.nonogram.impl

import org.scalacheck._
import Prop.forAll

object PositionSpec extends Properties("Position") {

  private val postitionGen = for {
    size: Int <- Gen.choose(5, 35)
    groupSize: Int <- Gen.choose(1, 11)
    group: Array[Int] <- Gen.containerOfN[Array, Int](groupSize, Gen.choose(1, size / groupSize + 1)) suchThat(group => group.sum + groupSize - 1 <= size)
  } yield Position(size, group)

  private def correctVariantElementsCount(p: Position): Boolean = {
    val groupSum = p.getGroup.sum
    p.getVariants.forall(variant => variant.size == groupSum)
  }

  private def variantsSorted(p: Position): Boolean = {
    p.getVariants.forall(variant => variant == variant.sorted)
  }

  private def correctIntervalSplit(p: Position): Boolean = {
    def distanceCorrect(splitInterval: Seq[Seq[Int]]): Boolean = {
      Array.range(1, splitInterval.size).forall(i => {
        splitInterval(i).head > splitInterval(i - 1).last + 1
      })
    }

    def intervalCorrect(interval: Seq[Int]): Boolean = {
      interval.size == 1 || Array.range(1, interval.size).forall(i => interval(i) - interval(i - 1) == 1)
    }

    def splitArray(seq: Seq[Int], splitSeq: Seq[Int], result: Seq[Seq[Int]]): Seq[Seq[Int]] = {
      if (seq.isEmpty || splitSeq.isEmpty) result
      else {
        splitArray(seq.drop(splitSeq.head), splitSeq.tail, result :+ seq.take(splitSeq.head))
      }
    }

    p.getVariants
      .map(variant => splitArray(variant, p.getGroup, Vector()))
      .forall(splitVariant => {
        splitVariant.forall(intervalCorrect) && distanceCorrect(splitVariant)
      })
  }

  property("Variants intersection is part of each variant") = forAll(postitionGen) { position =>
    val intersection = position.getIntersection
    intersection.isEmpty || position.getVariants.forall(variant => intersection.forall(variant.contains(_)))
  }

  property("Difference is not part of any variant") = forAll(postitionGen) { position =>
    val difference = position.getDifference
    difference.isEmpty || position.getVariants.forall(variant => difference.forall(!variant.contains(_)))
  }

  property("All variants are valid") = forAll(postitionGen) { position =>
    position.getVariantsCount > 0 && correctVariantElementsCount(position) && variantsSorted(position) && correctIntervalSplit(position)
  }


}
