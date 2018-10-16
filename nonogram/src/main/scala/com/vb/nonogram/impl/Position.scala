package com.vb.nonogram.impl

import com.vb.nonogram.impl.Position.Variant

import scala.collection.mutable.ArrayBuffer


class Position private[Position](val size: Int, val group: Seq[Int], val variants: Array[Variant]) {

  def getIntersection: Seq[Int] = {
    variants.reduceLeft((a, b) => a.intersect(b))
  }

  def getDifference: Seq[Int] = {
    val allVariants = variants.reduceLeft((a, b) => a.union(b))
    (0 until size).diff(allVariants)
  }

  def filterVariants(filledIndexes: Seq[Int], crossedOutIndexes: Seq[Int]): Position = {
    if (filledIndexes.isEmpty && crossedOutIndexes.isEmpty) {
      this
    } else {
      val filteredVariants = variants.filter(variant => {
        (filledIndexes.isEmpty || filledIndexes.forall(variant.contains(_))) && (crossedOutIndexes.isEmpty || crossedOutIndexes.forall(!variant.contains(_)))
      })
      new Position(size, group, filteredVariants)
    }
  }
}

object Position {
  type Variant = Array[Int]

  val EMPTY_POSITION = new Position(0, Array[Int](), Array())

  def apply(rowLength: Int, groups: Seq[Int]): Position = {
    val variants: ArrayBuffer[Array[Int]] = new ArrayBuffer[Array[Int]]()

    def computeVariant(group: Seq[Int], startIndex: Int, endIndex: Int, currentVariant: Array[Int]): Unit = {
      if (group.isEmpty) {
        variants.append(currentVariant)
      } else {
        val elemSum = group.map(_ + 1).sum - 1
        for (i <- startIndex to (endIndex - elemSum + 1)) {
          val increment = if (group.length == 1) 0 else 1
          computeVariant(group.tail, i + group.head + increment, endIndex, currentVariant ++ Array.range(i, group.head + i))
        }
      }
    }

    computeVariant(groups, 0, rowLength - 1, Array())
    new Position(rowLength, groups, variants.toArray)
  }
}
