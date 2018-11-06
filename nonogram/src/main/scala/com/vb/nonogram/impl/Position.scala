package com.vb.nonogram.impl

import com.vb.nonogram.impl.Position.Variant

import scala.collection.immutable.BitSet
import scala.collection.mutable.ArrayBuffer

class Position private[Position](private[this] val size: Int, private[this] val group: Seq[Int], private[this] val variants: Array[Variant]) {
  private[this] val allIndexes = (0 until size).foldLeft(BitSet())(_ + _)

  def getIntersection: Seq[Int] = {
    variants.reduceLeft((a, b) => a intersect b).toArray
  }

  def getDifference: Seq[Int] = {
    val allVariants = variants.reduceLeft((a, b) => a.union(b))
    allIndexes.diff(allVariants).toArray
  }

  def getVariants: Seq[Seq[Int]] = {
    variants.map(v => v.toIndexedSeq)
  }

  def getVariantsCount: Int = variants.length

  def getSize: Int = size

  def getGroup: Seq[Int] = group

  def filterVariants(filledIndexes: Seq[Int], crossedOutIndexes: Seq[Int]): Position = {
    if (filledIndexes.isEmpty && crossedOutIndexes.isEmpty) {
      this
    } else {
      val filteredVariants = variants.filter(variant => {
        (filledIndexes.isEmpty || filledIndexes.forall(variant.contains)) && (crossedOutIndexes.isEmpty || crossedOutIndexes.forall(!variant.contains(_)))
      })
      new Position(size, group, filteredVariants)
    }
  }
}

object Position {
  type Variant = BitSet

  val EMPTY_POSITION = new Position(0, Array[Int](), Array())

  def apply(rowLength: Int, groups: Seq[Int]): Position = {
    val variants: ArrayBuffer[Variant] = new ArrayBuffer[Variant]()

    def computeVariant(group: Seq[Int], startIndex: Int, endIndex: Int, currentVariant: Variant): Unit = {
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

    computeVariant(groups, 0, rowLength - 1, BitSet())
    new Position(rowLength, groups, variants.toArray)
  }
}
