package org.learningconcurrency.traditional_concurrency.helpers

object DefaultAtomicRefCounters {

  implicit val arrayOfAtomicsTraverser = new AtomicRefCounter[Array, MyAtomicReference[(List[Int], Long)]] {
    override def countGets(container: Array[MyAtomicReference[(List[Int], Long)]]): Int = container map(_.counter()) sum
  }
}