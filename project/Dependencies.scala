import sbt._
import Keys._

object Dependencies {

  val scalaTestVersion = "3.0.5"
  val scalaCheckVersion = "1.14.0"
  val akkaVersion = "2.4.19"
  val monixVersion = "2.3.0"
  //lowest stable version for scala 2.12
  val scalaAsyncVersion = "0.9.6"
  val catsVersion = "2.2.0"
  //2.12 version of rxScala
  val rxScalaVersion = "0.26.5"
  val scalazVersion = "7.3.2"

  val commonDependencies: Seq[ModuleID] = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test"
  )

  val akkaDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion)


  val monixDependencies: Seq[ModuleID] = Seq(
  "io.monix" %% "monix" % monixVersion,
  "io.monix" %% "monix-cats" % monixVersion
  )

  val scalaAsync = "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion

  val rxScala = "io.reactivex" %% "rxscala" % rxScalaVersion

  val scalaMeter = "com.storm-enroute" %% "scalameter" % "0.9"

  val scalaFx = "org.scalafx" %% "scalafx" % "11-R16"

  val cats = "org.typelevel" %% "cats-core" % catsVersion

  val scalazCore = "org.scalaz" %% "scalaz-core" % scalazVersion
}
