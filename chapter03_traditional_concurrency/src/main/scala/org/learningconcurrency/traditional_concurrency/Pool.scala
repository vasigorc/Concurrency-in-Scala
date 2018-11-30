package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

import org.learningconcurrency.traditional_concurrency.aop.ArrayOfAtomicsWrapper

import scala.annotation.tailrec

class Pool[T] extends ArrayOfAtomicsWrapper[(List[T], Long)]{

  type TimeStampedList = (List[T], Long)

  val parallelism: Int = Runtime.getRuntime.availableProcessors * 32
  val buckets = new Array[AtomicReference[TimeStampedList]](parallelism)
  for (i <- buckets.indices) buckets(i) = new AtomicReference((Nil, 0L))
  private val nonEmptyIndices = ConcurrentHashMap.newKeySet[Int]

  private def compactNonEmptyIndices(index: Int, newList: List[T]) = {
    newList match {
      case Nil => nonEmptyIndices.remove(index)
      case _ => nonEmptyIndices.add(index)
    }
  }

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
      else compactNonEmptyIndices(i, nlst)
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
          get(i) match {
            case (Nil, stamp) =>
              sum += stamp
              None
            case v@(lst, stamp) =>
              val nv = (lst.tail, stamp + 1)
              if (bucket.compareAndSet(v, nv)) {
                compactNonEmptyIndices(i, nv._1)
                Some(lst.head)
              }
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
    Ex 3.11 Currently, the remove operation of the lock-free pool implementation from this chapter runs in O(P) worst-case
    time, where P is the number of processors on the machine. Improve the lock-free pool implementation so that the
    operations run in O(1) expected time, both in terms of the number of stored elements and the number of processors.
   */
  def removeO1(): Option[T] = {
    if(nonEmptyIndices.isEmpty) None
    else {
      val index: Int = nonEmptyIndices.iterator().next()
      val bucket = get(index)

      @tailrec def retry(): Option[T] = {
        bucket._1 match {
          case Nil => None
          case head::tail =>
            if(buckets(index).compareAndSet(bucket, (tail, bucket._2))) {
              compactNonEmptyIndices(index, tail)
              Some(head)
            }
            else retry()
        }
      }

      retry() match {
        case removed @Some(_) => removed
        case None => None
      }
    }
  }
  /*
    Augment the lock-free pool implementation from this chapter with a foreach operation, used to traverse all the
    elements in the pool. Then make another version of foreach that is both lock-free and linearizable.
   */
  def foreach[U](tlFunc: T => U): Unit = {
    buckets.map(_ get()) flatMap (_._1) foreach tlFunc
  }

  def get(index: Int): TimeStampedList = buckets(index).get()
}