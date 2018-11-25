package org.learningconcurrency.traditional_concurrency.aop

import java.util.concurrent.atomic.{AtomicInteger => AInt, AtomicReference => ARef}

class AopARef[V] extends ARef[V]{

  private val getCounter: AInt = new AInt(0)

  def getAndListen(): V = {
    getCounter.getAndIncrement()
    super.get()
  }

  def counter(): Int = getCounter.get()

  def resetCounter(): Unit = getCounter.set(0)
}
