package org.learningconcurrency.traditional_concurrency

import org.learningconcurrency.BaseSpec

//test class for Exercise 1
class PiggybackContextSpec extends BaseSpec{

  trait PiggybackContextBuilder {
    val builder = new PiggybackContext
  }

  "Calling a runnable on PiggybackContext" should "be executed on the current thread" in new PiggybackContextBuilder {
    private val currentThread: Thread = Thread.currentThread()

    builder.execute(new Runnable {
      override def run(): Unit = {
        val pbThread: Thread = Thread.currentThread()
        pbThread shouldEqual(currentThread)
      }
    })
  }
}
