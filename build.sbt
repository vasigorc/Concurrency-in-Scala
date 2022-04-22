ThisBuild / name := "learning concurrent programming in scala"
ThisBuild / organization in ThisBuild := "ca.vgorcinschi"
ThisBuild / scalaVersion := "2.13.8"

ThisBuild / version := "0.0.1"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

lazy val chapter01_introduction = project.
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.akkaDependencies)

lazy val chapter02_concurrency_on_jvm = project.
  dependsOn(chapter01_introduction).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.commonDependencies)

lazy val chapter03_traditional_concurrency = project.
  dependsOn(chapter01_introduction % "compile->compile;test->test").
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.commonDependencies ++ Dependencies.monixDependencies)

lazy val chapter04_futures_and_promises = project.
  dependsOn(chapter01_introduction % "compile->compile;test->test").
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.commonDependencies :+ Dependencies.scalaAsync)

lazy val chapter05_data_parallel_collections = project
  .dependsOn(chapter01_introduction % "compile->compile;test->test")
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.commonDependencies :+ Dependencies.scalaMeter)

lazy val root = (project in file(".")).
  aggregate(chapter01_introduction,
    chapter02_concurrency_on_jvm,
    chapter03_traditional_concurrency,
    chapter04_futures_and_promises,
    chapter05_data_parallel_collections)
