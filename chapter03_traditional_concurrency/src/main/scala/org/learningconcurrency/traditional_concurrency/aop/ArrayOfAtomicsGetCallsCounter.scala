package org.learningconcurrency.traditional_concurrency.aop

import java.util.concurrent.atomic.AtomicInteger

trait ArrayOfAtomicsGetCallsCounter[T] extends ArrayOfAtomicsWrapper[T] {

  private val counter: AtomicInteger = new AtomicInteger(0)

  abstract override def get(index: Int): T = {
    counter.getAndIncrement()
    super.get(index)
  }

  def getGetCallsCounter: Int = counter.get()

  def resetGetCallsCounter(): Unit = counter.set(0)
}
