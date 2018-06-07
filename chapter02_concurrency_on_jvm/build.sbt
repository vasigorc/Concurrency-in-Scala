import Dependencies._

lazy val chapter_01 = RootProject(file("../chapter01_introduction"))

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "ca.vgorcinschi",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "chapter02_concurrency_on_jvm",
    libraryDependencies += scalaTest % Test,
    mainClass in Compile := Some("org.learningconcurrency.concurrency_on_jvm.PriorityTaskPool")
  ).dependsOn(chapter_01)
