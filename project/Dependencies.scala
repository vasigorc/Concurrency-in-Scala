import sbt._

object Dependencies {

  val scalaTestVersion = "3.2.11"
  val scalaCheckVersion = "1.14.0"
  val akkaVersion = "2.6.0"
  val monixVersion = "3.0.0"
  val scalaAsyncVersion = "0.10.0"
  val catsVersion = "2.2.0"
  val rxScalaVersion = "0.27.0"
  val scalazVersion = "7.3.2"

  val commonDependencies: Seq[ModuleID] = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"
  )

  val akkaDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion)


  val monixDependencies: Seq[ModuleID] = Seq(
  "io.monix" %% "monix" % monixVersion
  )

  val scalaAsync = "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion

  val rxScala = "io.reactivex" %% "rxscala" % rxScalaVersion

  val scalaMeter = "com.storm-enroute" %% "scalameter" % "0.19"

  val scalaFx = "org.scalafx" %% "scalafx" % "12.0.2-R18"

  val cats = "org.typelevel" %% "cats-core" % catsVersion

  val scalazCore = "org.scalaz" %% "scalaz-core" % scalazVersion
}
