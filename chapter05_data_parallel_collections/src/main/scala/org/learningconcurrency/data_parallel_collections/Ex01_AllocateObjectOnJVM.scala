package org.learningconcurrency.data_parallel_collections

import org.scalameter._

/**
  * Exercise 1: Measure the average running time for allocating a simple object on the JVM.
  *
  */
object Ex01_AllocateObjectOnJVM extends App {

  val time: Quantity[Double] = config(
    Key.exec.benchRuns -> 20,
    Key.verbose -> true
  ) withWarmer {
    new Warmer.Default
  } withMeasurer {
    new Measurer.IgnoringGC
  } measure {
    var i: Object = null
    i = new Object
  }

  println(s"Total time: $time")
}
