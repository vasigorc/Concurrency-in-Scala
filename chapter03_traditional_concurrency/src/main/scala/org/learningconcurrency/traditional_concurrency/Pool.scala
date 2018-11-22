package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

class Pool[T] {

  type TimeStampedList = (List[T], Long)

  val parallelism: Int = Runtime.getRuntime.availableProcessors * 32
  val buckets = new Array[AtomicReference[TimeStampedList]](parallelism)
  for (i <- buckets.indices) buckets(i) = new AtomicReference((Nil, 0L))

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
          case removed@Some(_) => return removed
          case None =>
        }

        i = (i + 1) % buckets.length
      }
      if (sum == witness) None
      else scan(sum)
    }

    scan(-1L)
  }

  /*
    Augment the lock-free pool implementation from this chapter with a foreach operation, used to traverse all the
    elements in the pool. Then make another version of foreach that is both lock-free and linearizable.
   */
  def foreach[U](tlFunc: T => U): Unit = {
    buckets.map(_ get()) flatMap (_._1) foreach tlFunc
  }
}