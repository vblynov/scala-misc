package com.vb.sudoku

import org.scalacheck.{Gen, Prop, Test}
import org.scalacheck.commands.Commands

import scala.util.{Success, Try}

class FieldWrapper(var field: Field, val size: Int) {
  def setCorrectValue(row: Int, col: Int, value: Int): Boolean = {
    field = field.setValue(row, col, value)
    !field.isWrong
  }

  def reset(): Unit = {
    field = Field(size)
  }
}

object FiledCommands extends org.scalacheck.Properties("FieldCommands") {
  property("FieldSpec") = FieldSpecification.property()

  override def overrideParameters(p: Test.Parameters): Test.Parameters = p.withMaxDiscardRatio(200).withMinSuccessfulTests(20)
}

object FieldSpecification extends Commands {
  private[this] val SIZE = 25

  override type Sut = FieldWrapper
  case class State (body: Array[Int])

  override def canCreateNewSut(newState: State, initSuts: Traversable[State], runningSuts: Traversable[Sut]): Boolean = initSuts.isEmpty && runningSuts.isEmpty

  override def newSut(state: State): Sut = new FieldWrapper(Field(state.body), SIZE)

  override def destroySut(sut: Sut): Unit = {}

  override def initialPreCondition(state: State): Boolean = true

  override def genInitialState: Gen[State] = State(Array.fill(SIZE * SIZE)(0))

  def genSetValue(state: State): Gen[SetValue] = for (
    row <- Gen.choose(0, SIZE - 1);
    col <- Gen.choose(0, SIZE - 1);
    value <- Gen.choose(1, SIZE) suchThat(v => notInRow(state.body, row, v) && notInCol(state.body, col, v) && notInSector(state.body, row, col, v))
  ) yield SetValue(row, col, value)

  override def genCommand(state: State): Gen[Command] = Gen.frequency((3, genSetValue(state)), (1, Gen.const(Reset())))

  def notInRow(body: Array[Int], row: Int, value: Int): Boolean = !(for (i <- row * SIZE until row * SIZE + SIZE) yield body(i)).contains(value)

  def notInCol(body: Array[Int], col: Int, value: Int): Boolean =  !(for (i <- col to SIZE * (SIZE - 1) + col by SIZE) yield body(i)).contains(value)

  def notInSector(body: Array[Int], row: Int, col: Int, value: Int): Boolean = {
    val sector = Field.resolveSector(row, col, SIZE)
    !(for (i <- 0 until SIZE; j <- 0 until SIZE if Field.resolveSector(i, j, SIZE) == sector) yield body(i * SIZE + j)).contains(value)
  }

  def isValidSudokuField(field: IndexedSeq[Int]): Boolean = {
    def isValidValues(values: IndexedSeq[Int]) : Boolean = {
      val valueWithoutZeroes = values.filter(_ != 0)
      valueWithoutZeroes.distinct.size == valueWithoutZeroes.size
    }
    val size = Math.sqrt(field.length).toInt
    val sqSize = Math.sqrt(size).toInt

    val columns = for (i <- 0 until size; j <- 0 until size) yield field(i + size * j)
    val sectors = for (st <- 0 until size by sqSize; i <- 0 until sqSize; j <- 0 until sqSize) yield field(st + i*size + j)

    val evalStream = field #:: columns #:: sectors #:: Stream.empty[IndexedSeq[Int]]
    evalStream.forall(v => v.grouped(size).forall(isValidValues))
  }

  case class SetValue(row: Int, col: Int, value: Int) extends Command {
    type Result = Boolean

    override def run(sut: FieldWrapper): Boolean = sut.setCorrectValue(row, col, value)

    override def nextState(state: State): State = State(state.body.updated(row * SIZE + col, value))

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Boolean]): Prop = {
      result == Success(true) && isValidSudokuField(state.body)
    }
  }

  case class Reset() extends UnitCommand {

    override def postCondition(state: State, success: Boolean): Prop = true

    override def run(sut: FieldWrapper): Unit = sut.reset()

    override def nextState(state: State): State = State(Array.fill(SIZE * SIZE)(0))

    override def preCondition(state: State): Boolean = true
  }
}