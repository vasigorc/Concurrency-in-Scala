package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.atomic.AtomicReference

/*
  Exercise 5. Implement a LazyCell class. Creating a LazyCell object and calling the apply method must
  have the same semantics as declaring a lazy value and reading it respectively

  You are not allowed to use lazy keyword
 */
class LazyCell[T](initialization: =>T) {

  private val atomicInit = new AtomicReference[T](initialization)

  def apply(): T = atomicInit.get()
}
