package org.learningconcurrency.data_parallel_collections.binaryheapsplitter

import scala.annotation.tailrec

object BinaryHeapSplitter {
  type IsOrdered[A] = A => Ordered[A]

  implicit class HeapOps[+A : IsOrdered](heap: Heap[A]) {
    def toStream: LazyList[A] = {
      @tailrec
      def loop(diminishingHeap: Heap[A], acc: LazyList[A]): LazyList[A] = {
        println(diminishingHeap.size)
        if (diminishingHeap.isEmpty) {
          println("exiting")
          acc
        }
        else loop(diminishingHeap.remove, diminishingHeap.min +: acc )
      }

      loop(heap, LazyList.empty)
    }

    def par[K >: A]: ParHeap[K] = ParHeap(heap)
  }
}
