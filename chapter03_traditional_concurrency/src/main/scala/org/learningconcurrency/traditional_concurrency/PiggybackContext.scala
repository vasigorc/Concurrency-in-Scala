package org.learningconcurrency.traditional_concurrency

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}
/*
  Exercise 1. Implement a custom ExecutionContext class called PiggybackContext, which executes Runnable objects
  on the same thread that calls the execute method. Ensure that a Runnable object executin on the Piggyback context can
  also call the execute method and that exceptions are properly reported
*/
class PiggybackContext extends ExecutionContext{

  import org.learningconcurrency._

  override def execute(runnable: Runnable): Unit = Try(runnable.run()) match {
    case Success(r) => log(s"Succesfully executed task ${r.##}")
    case Failure(t) => reportFailure(t)
  }

  override def reportFailure(cause: Throwable): Unit = log(s"Execution failure: ${cause.getMessage}")
}
