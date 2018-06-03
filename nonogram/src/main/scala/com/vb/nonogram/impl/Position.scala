package com.vb.nonogram.impl

import scala.collection.mutable.ArrayBuffer

class Position private(val variants: Array[Array[Range]]) {

  def initialVariants: Iterable[Array[Range]] = variants

}

object Position {
  def apply(rowLength: Int, groups: Array[Int]): Position = {
    val variants: ArrayBuffer[Array[Range]] = new ArrayBuffer[Array[Range]]()
    def computeVariant(group: Array[Int], startIndex: Int, endIndex: Int, currentVariant: Array[Range]): Unit = {
      if (group.isEmpty) {
        variants.append(currentVariant)
      } else {
        val elemSum = group.map(_ + 1).sum - 1
        for (i <- startIndex to (endIndex - elemSum + 1)) {
          val range = Range(i, group(0) + i)
          val increment = if (group.length == 1) 0 else 1
          computeVariant(group.tail, i + group.head + increment, endIndex, currentVariant :+ range)
        }
      }
    }
    computeVariant(groups, 0, rowLength - 1, Array())
    new Position(variants.toArray)
  }
}
