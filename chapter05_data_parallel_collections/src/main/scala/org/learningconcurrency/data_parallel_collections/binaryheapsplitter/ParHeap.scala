package org.learningconcurrency.data_parallel_collections.binaryheapsplitter

import org.learningconcurrency.data_parallel_collections.binaryheapsplitter.BinaryHeapSplitter._

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable
import scala.collection.parallel.SeqSplitter
import scala.collection.parallel.immutable.ParSeq

class ParHeap[+A : IsOrdered](private val heap: Heap[A]) extends ParSeq[A] {
  override def apply(i: Int): A = seq(i)

  /** Transforming [[Stream]] to [[Vector]] once to gain O(1) random
    * access complexity in every downstream operation
    * @return
    */
  override def splitter: SeqSplitter[A] =
    new ParHeapSplitter(Vector.from(seq), 0, length)

  override def seq: immutable.Seq[A] = heap.toStream

  override def length: Int = seq.size

  class ParHeapSplitter(elements: Vector[A], i: Int, limit: Int)
      extends SeqSplitter[A] {

    private val currentIndex = new AtomicInteger(i)

    override def dup: SeqSplitter[A] =
      new ParHeapSplitter(elements, currentIndex.get, limit)

    override def split: Seq[SeqSplitter[A]] = {
      val nrElemsRemaining: Int = remaining
      if (nrElemsRemaining >= 2)
        psplit(nrElemsRemaining / 2, nrElemsRemaining - nrElemsRemaining / 2)
      else Seq(this)
    }

    override def psplit(sizes: Int*): Seq[SeqSplitter[A]] = {
      val subSplitters = for (sz <- sizes) yield {
        val newLimit = math.min(currentIndex.get + sz, limit)
        val ps       = new ParHeapSplitter(elements, currentIndex.get, newLimit)
        // Update currentIndex!
        currentIndex.set(newLimit)
        ps
      }

      if (currentIndex.get == limit) subSplitters
      else
        subSplitters :+ new ParHeapSplitter(elements, currentIndex.get, limit)
    }

    override def remaining: Int = limit - currentIndex.get()

    override def hasNext: Boolean = currentIndex.get < limit

    override def next(): A = elements(currentIndex.getAndIncrement())
  }
}

object ParHeap {
  def apply[A : IsOrdered](heap: Heap[A]): ParHeap[A] = new ParHeap[A](heap)
}
