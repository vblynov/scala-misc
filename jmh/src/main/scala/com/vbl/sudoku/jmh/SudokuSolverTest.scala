package com.vbl.sudoku.jmh

import java.util.concurrent.TimeUnit

import com.vbl.sudoku.impl.{Field, ParallelSudokuSolver, SequentialSudokuSolver}
import org.openjdk.jmh.annotations._

@Fork(1)
@State(Scope.Benchmark)
@Threads(1)
@Warmup(iterations = 10, time=1, timeUnit=TimeUnit.SECONDS)
@Measurement(iterations = 10, time=1, timeUnit=TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class SudokuSolverTest {

  val field = Array(
    Field("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0"),

    //hard
    Field("0 2 0 0 0 0 0 0 0 0 0 0 6 0 0 0 0 3 0 7 4 0 8 0 0 0 0 0 0 0 0 0 3 0 0 2 0 8 0 0 4 0 0 1 0 6 0 0 5 0 0 0 0 0 0 0 0 0 1 0 7 8 0 5 0 0 0 0 9 0 0 0 0 0 0 0 0 0 0 4 0"),
    Field("0 0 0 0 0 0 0 0 0 0 0 0 0 2 0 0 3 0 2 8 0 0 0 0 5 0 6 0 0 0 0 0 0 0 0 7 0 0 6 8 5 0 0 4 0 1 0 4 6 0 7 0 0 0 0 5 0 3 0 0 0 9 0 0 0 1 0 0 2 0 0 0 6 3 7 9 0 0 1 0 0"),

    //medium
    Field("0 0 0 3 0 9 4 8 0 0 0 0 1 0 0 0 0 0 0 1 3 7 0 0 0 0 9 2 4 0 0 0 0 0 5 0 0 9 0 2 0 1 0 3 0 0 3 0 0 0 0 0 1 6 8 0 0 0 0 5 7 4 0 0 0 0 0 0 8 0 0 0 0 5 4 6 0 7 0 0 0"),
    Field("6 0 1 0 9 5 2 0 4 0 2 4 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 8 0 0 0 6 0 3 6 0 0 0 4 7 0 8 0 0 0 6 0 0 0 9 0 0 0 0 0 0 0 0 0 0 0 0 2 0 0 7 3 0 2 0 3 4 5 0 9 0 1"),

    //easy
    Field("4 0 0 0 0 0 6 5 0 0 2 9 0 5 0 0 0 0 3 0 0 4 9 7 0 0 0 6 0 0 0 4 2 0 0 0 8 9 0 0 0 0 0 6 7 0 0 0 6 7 0 0 0 3 0 0 0 2 3 1 0 0 6 0 0 0 0 8 0 2 7 0 0 1 8 0 0 0 0 0 5"),
    Field("0 9 0 0 5 6 0 0 2 3 0 0 0 0 8 0 1 0 0 0 0 0 7 0 0 9 0 0 0 0 0 9 0 0 0 6 0 0 9 8 0 1 2 0 0 5 0 0 0 3 0 0 0 0 0 7 0 0 8 0 0 0 0 0 8 0 2 0 0 0 0 9 2 0 0 6 1 0 0 3 0")
  )

  val solvedField = Field("1 2 6 4 3 7 9 5 8 8 9 5 6 2 1 4 7 3 3 7 4 9 8 5 1 2 6 4 5 7 1 9 3 8 6 2 9 8 3 2 4 6 5 1 7 6 1 2 5 7 8 3 9 4 2 6 9 3 1 4 7 8 5 5 4 8 7 6 9 2 3 1 7 3 1 8 5 2 6 4 9")

  val seqSolver = new SequentialSudokuSolver()
  val parSolver = new ParallelSudokuSolver()

  @Param(Array("1", "2", "3", "4", "5", "6"))
  var sampleIndex:Int = _

  @Benchmark
  def testSequentialSolver(): Field = {
    seqSolver.solve(field(sampleIndex))
  }

  @Benchmark
  def testParallelSolver(): Field = {
    parSolver.solve(field(sampleIndex))
  }

  @Benchmark
  def testSequentialSolverBaseline(): Field = {
    seqSolver.solve(solvedField)
  }

  @Benchmark
  def testParallelSolverBaseline(): Field = {
    parSolver.solve(solvedField)
  }

}
