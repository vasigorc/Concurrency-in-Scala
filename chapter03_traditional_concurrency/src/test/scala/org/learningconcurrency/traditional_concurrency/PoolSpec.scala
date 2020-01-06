package org.learningconcurrency.traditional_concurrency

import org.learningconcurrency.traditional_concurrency.aop.{ArrayOfAtomicsGetCallsCounter, ArrayOfAtomicsWrapper}
import org.learningconcurrency.{BaseSpec, thread}

class PoolSpec extends BaseSpec {

  trait IntPoolBuilder {
    val pool = new Pool[Int] with ArrayOfAtomicsWrapper[(List[Int], Long)] with ArrayOfAtomicsGetCallsCounter[(List[Int], Long)]
    val p = 8
    val num = 1000
  }

  behavior of "foreach"

  it should "help to correctly count sum of ints pushed to the pool" in new IntPoolBuilder {

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
    pool.add(1)
    pool.resetGetCallsCounter()
    pool.removeO1() should be (Some(1))
    pool.getGetCallsCounter should equal (1)
  }

  behavior of "remove"

  "method" should "not have O(1) complexity" in new IntPoolBuilder {
    pool.add(1)
    pool.resetGetCallsCounter()
    pool.remove() should be (Some(1))
    pool.getGetCallsCounter should be > 1
  }
}