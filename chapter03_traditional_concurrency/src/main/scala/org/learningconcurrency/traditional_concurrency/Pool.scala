package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

class Pool[T] {

  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val buckets = new Array[AtomicReference[(List[T], Long)]](parallelism)
  for (i <- 0 until buckets.length) buckets(i) = new AtomicReference((Nil, 0L))

  def add(x: T): Unit = {
    val i = (Thread.currentThread.getId ^ x.## % buckets.length).toInt

    @tailrec def retry(): Unit = {
      val bucket = buckets(i)
      val v = bucket.get
      val (lst, stamp) = v
      val nlst = x :: lst
      val nstamp = stamp + 1
      val nv = (nlst, nstamp)
      if (!bucket.compareAndSet(v, nv)) retry()
    }

    retry()
  }

  def remove(): Option[T] = {
    val start = (Thread.currentThread.getId % buckets.length).toInt

    @tailrec def scan(witness: Long): Option[T] = {
      var i = (start + 1) % buckets.length
      var sum = 0L
      while (i != start) {
        val bucket = buckets(i)

        @tailrec def retry(): Option[T] = {
          bucket.get match {
            case (Nil, stamp) =>
              sum += stamp
              None
            case v@(lst, stamp) =>
              val nv = (lst.tail, stamp + 1)
              if (bucket.compareAndSet(v, nv)) Some(lst.head)
              else retry()
          }
        }

        retry() match {
          case removed@Some(value) => return removed
          case None =>
        }

        i = (i + 1) % buckets.length
      }
      if (sum == witness) None
      else scan(sum)
    }

    scan(-1L)
  }
}