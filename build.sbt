name := "scala-misc"
organization in ThisBuild := "com.vb"
scalaVersion in ThisBuild := "2.12.6"
version in ThisBuild := "0.1"

lazy val root = project
  .in(file("."))
  .aggregate(
    sudoku,
    jmh
  )

lazy val sudoku = project
  .settings(
    name := "sudoku",
    libraryDependencies ++= commonDependencies
  )

lazy val jmh = project
  .dependsOn(sudoku)
  .enablePlugins(JmhPlugin)
  .settings(
    name := "jmh"
  )


lazy val dependencies =
  new {
    val scalacticV       = "3.0.5"
    val scalatestV       = "3.0.5"
    val scalacheckV      = "1.13.5"

    val scalactic    = "org.scalactic"   %% "scalactic"               % scalacticV
    val scalatest    = "org.scalatest"   %% "scalatest"               % scalatestV
    val scalacheck   = "org.scalacheck"  %% "scalacheck"              % scalacheckV
  }

lazy val commonDependencies = Seq(
  dependencies.scalactic  % "test",
  dependencies.scalatest  % "test",
  dependencies.scalacheck % "test"
)