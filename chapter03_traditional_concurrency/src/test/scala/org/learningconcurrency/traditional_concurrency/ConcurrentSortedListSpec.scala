package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.ForkJoinPool

import org.learningconcurrency.BaseSpec

import scala.concurrent.ExecutionContext
import scala.util.Random

class ConcurrentSortedListSpec extends BaseSpec{

  trait Builder {
    val sample = new ConcurrentSortedList[Int]()
  }

  behavior of "ConcurrentSortedList of Ints accessed by multiple threads"

  it should "remain sorted when written to by 100 threads" in new Builder {
    private val exCtx = ExecutionContext.global
    for(i <- 0 until 100)
      exCtx.execute{
        new Runnable {
          override def run(): Unit = {
            val probe = Random.nextInt(200)
            sample.add(probe)
          }
        }
      }

    Thread.sleep(10000)
    private val toList: List[Int] = sample.iterator.toList
    info(s"$toList")
    toList shouldBe sorted
  }
}
