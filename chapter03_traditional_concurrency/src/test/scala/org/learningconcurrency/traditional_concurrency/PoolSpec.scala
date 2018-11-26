package org.learningconcurrency.traditional_concurrency

import org.learningconcurrency.traditional_concurrency.helpers.TraditionalConcurrencyHelpers
import org.learningconcurrency.{BaseSpec, thread}

class PoolSpec extends BaseSpec {

  trait IntPoolBuilder {
    val pool = new Pool[Int]
    val p = 8
    val num = 1000
  }

  behavior of "foreach"

  "method" should "help to correctly count sum of ints pushed to the pool" in new IntPoolBuilder {

    val inserters: Seq[Thread] = for (_ <- 0 until p) yield thread {
      for (_ <- 0 until num) pool add 1
    }

    inserters.foreach(_.join())

    var counter = 0
    pool.foreach(counter += _)

    counter shouldEqual num * p
  }

  behavior of "removeO1"

  "method" should "remove an element from the pool with time O(1)" in new IntPoolBuilder {
    import org.learningconcurrency.traditional_concurrency.helpers.DefaultAtomicRefCounters._
    import TraditionalConcurrencyHelpers._
    //call pool.removeO1 (not implemented yet)
    pool.buckets.countGets() shouldEqual 1
  }
}