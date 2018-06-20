package org.learningconcurrency.concurrency_on_jvm

import org.learningconcurrency.BaseSpec

import scala.util.Random

class Exercises13Spec extends BaseSpec{

  "concurrently adding and then swapping 1000 random values to ConcurrentBiMap" should "" +
    "keep the map size the same" in {
    val target = new ConcurrentBiMap[Long, Long]
    val workers = PriorityTaskPool_Ex9(4)
    (1 to 1000).foreach {i => workers.asynchronous(i){() => target.put(Random.nextLong(), Random.nextLong())}}
    Thread.sleep(1000)
    val sizeBefore = target.size
    target.iterator.zipWithIndex.foreach {case ((key, value), i) => workers.asynchronous(i){()=> target.replace(key, value, value, key)}}
    Thread.sleep(1000)
    val sizeAfter = target.size
    sizeBefore shouldEqual sizeAfter
  }
}
