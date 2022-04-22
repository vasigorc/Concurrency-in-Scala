import Dependencies.{scalaFx, scalazCore, cats}

name := "chapter05_data_parallel_collections"

// Add OS specific JavaFX dependencies
val javafxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}
libraryDependencies ++= javafxModules.map(m => "org.openjfx" % s"javafx-$m" % "11" classifier osName)
resolvers += Resolver.sonatypeRepo("snapshots")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")

libraryDependencies ++= Seq(scalaFx, scalazCore, cats)

// Add ScalaMeter to the testing Frameworks
testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

// from scalameter user guide: Latest sbt versions run tests in parallel by default. If there are
// multiple tests in project, than they will be executed in parallel during `sbt test` potentially
// making results useless. To disable this behavior we need to add the following:
Test / parallelExecution := false
logBuffered := false