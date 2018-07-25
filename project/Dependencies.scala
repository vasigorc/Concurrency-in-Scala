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
 
  val akkaDependencies  : Seq[ModuleID] = commonDependencies ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion)
}
