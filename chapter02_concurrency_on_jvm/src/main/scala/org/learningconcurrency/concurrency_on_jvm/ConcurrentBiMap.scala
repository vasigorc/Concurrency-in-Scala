package org.learningconcurrency.concurrency_on_jvm

import scala.collection.mutable.{HashMap => MyMap}

// for exercises 11 and 12
class ConcurrentBiMap [K, V] {
  private val byKeys = new MyMap[K, V]
  private val byValues = new MyMap[V, K]

  def put(k: K, v: V):Option[(K, V)] = ???
}
