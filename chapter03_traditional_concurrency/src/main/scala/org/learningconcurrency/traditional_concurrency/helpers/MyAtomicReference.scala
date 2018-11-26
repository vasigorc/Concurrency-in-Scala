package org.learningconcurrency.traditional_concurrency.helpers

import java.util.concurrent.atomic.{AtomicInteger => AInt, AtomicReference => ARef}

class MyAtomicReference[V](initialValue: V) extends ARef[V](initialValue) {

  private val getCounter: AInt = new AInt(0)

  def getAndListen(): V = {
    getCounter.getAndIncrement()
    super.get()
  }

  def counter(): Int = getCounter.get()

  def resetCounter(): Unit = getCounter.set(0)
}
