package org.learningconcurrency.traditional_concurrency.helpers

object TraditionalConcurrencyHelpers {
  implicit class CountArrayAtomicGetsOps(wrapper: Array[MyAtomicReference[(List[Int], Long)]]) {
    def countGets()(implicit atomicRefCounter: AtomicRefCounter[Array, MyAtomicReference[(List[Int], Long)]]): Int = atomicRefCounter.countGets(wrapper)
  }
}
