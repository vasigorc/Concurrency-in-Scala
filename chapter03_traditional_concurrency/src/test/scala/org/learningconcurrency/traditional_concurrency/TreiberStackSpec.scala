package org.learningconcurrency.traditional_concurrency

import org.scalatest.concurrent.Conductors
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TreiberStackSpec extends AnyFlatSpec with Matchers with Conductors {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new TreiberStack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() === 2)
    assert(stack.pop() === 1)
  }

  "A NoSuchElementException" should "be thrown when popping from an empty stack" in {
    val stack = new TreiberStack[Int]
    an [NoSuchElementException] should be thrownBy stack.pop()
  }

  "Asyn test TreiberStack with backpressured Producer-Consumer" should "be same as synchronous" in {
    val conductor = new Conductor
    import conductor._

    val stack = new TreiberStack[Int]

    threadNamed("producer"){
      stack.push(1)
      stack.push(2)

      beat should be (1)
    }

    threadNamed("consumer"){
      waitForBeat(1)

      stack.pop() should be (2)
      stack.pop() should be (1)

      whenFinished{
        stack should be ('empty)
      }
    }
  }
}
