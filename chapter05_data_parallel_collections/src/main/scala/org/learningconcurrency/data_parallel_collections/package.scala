package org.learningconcurrency

import org.scalameter.{Key, Measurer, Warmer, config}

package data_parallel_collections {
  case class TimedResult[T](result: T, time: Double)
}

package object data_parallel_collections {

  def getTimedResult[T](body: => T): TimedResult[T] = {
    var result: Option[T] = None

    val time = config (
      Key.exec.benchRuns -> 20,
      Key.verbose -> true
    ) withWarmer {
      new Warmer.Default
    } withMeasurer {
      new Measurer.IgnoringGC
    } measure {
      result = Some(body)
    }
    TimedResult(result.get, time.value)
  }
}