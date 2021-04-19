package org.learningconcurrency.data_parallel_collections.binaryheapsplitter

import scalaz.Heap

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable
import scala.collection.parallel.SeqSplitter
import scala.collection.parallel.immutable.ParSeq

class ParHeap[A](private val heap: Heap[A]) extends ParSeq[A] {
  override def apply(i: Int): A = seq(i)

  /** Transforming [[Stream]] to [[Array]] once to gain O(1) random
    * access complexity in every downstream operation
    * @return
    */
  override def splitter: SeqSplitter[A] =
    ParHeapSplitter(seq.toArray, 0, length)

  override def seq: immutable.Seq[A] = heap.toStream

  override def length: Int = seq.size

  class ParHeapSplitter(elements: Array[A], i: Int, limit: Int)
      extends SeqSplitter[A] {

    private val currentIndex = new AtomicInteger(i)

    override def dup: SeqSplitter[A] =
      ParHeapSplitter(elements, currentIndex.get, limit)

    override def split: Seq[SeqSplitter[A]] = {
      val nrElemsRemaining: Int = remaining
      if (nrElemsRemaining >= 2)
        psplit(nrElemsRemaining / 2, nrElemsRemaining - nrElemsRemaining / 2)
      else Seq(this)
    }

    override def psplit(sizes: Int*): Seq[SeqSplitter[A]] = {
      val subSplitters = for (sz <- sizes) yield {
        val newLimit = math.min(currentIndex.get + sz, limit)
        val ps       = ParHeapSplitter(elements, currentIndex.get, newLimit)
        // Update currentIndex!
        currentIndex.set(newLimit)
        ps
      }

      if (currentIndex.get == limit) subSplitters
      else
        subSplitters :+ ParHeapSplitter(elements, currentIndex.get, limit)
    }

    override def remaining: Int = limit - currentIndex

    override def hasNext: Boolean = currentIndex.get < limit

    override def next(): A = elements(currentIndex.getAndIncrement())
  }

  object ParHeapSplitter {
    def apply(elements: Array[A], i: Int, limit: Int) =
      new ParHeapSplitter(elements, i, limit)
  }
}
