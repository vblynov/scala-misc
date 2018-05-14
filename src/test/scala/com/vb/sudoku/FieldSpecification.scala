package com.vb.sudoku

import org.scalacheck.{Gen, Prop}
import org.scalacheck.Gen.{const, frequency, identifier, nonEmptyListOf, oneOf, someOf}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.commands.Commands

import scala.util.Try

class FieldWrapper(var field: Field) {

  def setValue(row: Int, col: Int, value: Int): Unit = {
    field = field.setValue(row, col, value)
  }

  def isWrong: Boolean = field.isWrong
}

object FieldSpecification extends Commands {

  override type State = Field
  override type Sut = FieldWrapper

  override def canCreateNewSut(newState: State, initSuts: Traversable[State], runningSuts: Traversable[Sut]): Boolean = initSuts.isEmpty && runningSuts.isEmpty

  override def newSut(state: State): Sut = new FieldWrapper(state)

  override def destroySut(sut: Sut): Unit = {}

  override def initialPreCondition(state: State): Boolean = !state.isWrong

  override def genInitialState: Gen[State] = Field(9)

  override def genCommand(state: Field): Gen[Command] = ???

  case class SetCorrectValue(row: Int, col: Int, value: Int) extends UnitCommand {

    override def run(sut: FieldWrapper): Unit = ???

    override def nextState(state: Field): Field = ???

    override def preCondition(state: Field): Boolean = ???

    override def postCondition(state: Field, success: Boolean): Prop = ???
  }
}
