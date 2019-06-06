import sbt._
import Keys._

object Dependencies {

  val scalaTestVersion = "3.0.5"
  val scalaCheckVersion = "1.14.0"
  val commonDependencies: Seq[ModuleID] = Seq(
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test"
  )

  val akkaVersion = "2.4.19"

  val akkaDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion)

  val monixVersion = "2.3.0"

  val monixDependencies: Seq[ModuleID] = Seq(
    "io.monix" %% "monix" % monixVersion,
    "io.monix" %% "monix-cats" % monixVersion
  )

  //lowest stable version for scala 2.12
  val scalaAsyncVersion = "0.9.6"

  val scalaAsync = "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion

  //2.12 version of rxScala
  val rxScalaVersion = "0.26.5"

  val rxScala = "io.reactivex" %% "rxscala" % rxScalaVersion

  val scalaMeter = "com.storm-enroute" %% "scalameter" % "0.9"
}
