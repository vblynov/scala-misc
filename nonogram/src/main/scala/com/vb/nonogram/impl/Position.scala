package com.vb.nonogram.impl

import com.vb.nonogram.impl.Position.Variant

import scala.collection.mutable.ArrayBuffer


class Position private[Position](val size: Int, val group: Array[Int], val variants: Array[Variant]) {

  def getIntersection: Seq[Int] = {
    variants.reduceLeft((a, b) => a.intersect(b))
  }

  def filterVariants(markedIndexes: Seq[Int]): Position = {
    val filteredVariants = variants.filter(variant => {
      markedIndexes.forall(variant.contains(_))
    })
    new Position(size, group, filteredVariants)
  }
}

object Position {
  type Variant = Array[Int]

  def apply(rowLength: Int, groups: Array[Int]): Position = {
    val variants: ArrayBuffer[Array[Int]] = new ArrayBuffer[Array[Int]]()
    def computeVariant(group: Array[Int], startIndex: Int, endIndex: Int, currentVariant: Array[Int]): Unit = {
      if (group.isEmpty) {
        variants.append(currentVariant)
      } else {
        val elemSum = group.map(_ + 1).sum - 1
        for (i <- startIndex to (endIndex - elemSum + 1)) {
          val increment = if (group.length == 1) 0 else 1
          computeVariant(group.tail, i + group.head + increment, endIndex, currentVariant ++ Array.range(i, group(0) + i))
        }
      }
    }
    computeVariant(groups, 0, rowLength - 1, Array())
    new Position(rowLength, groups, variants.toArray)
  }
}
