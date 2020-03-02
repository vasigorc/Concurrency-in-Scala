package org.learningconcurrency.data_parallel_collections.parstring

import scala.collection.parallel.immutable.ParSeq
import scala.collection.{immutable, parallel}
import scala.collection.parallel.{Combiner, SeqSplitter, immutable}

/**
 * Exercise 6. Explain how can you improve the performance of the
 * `result` method in the ParStringCombiner class, as shown in this chapter.
 * Can you parallelize this method?
 * The solution is the use of `par.aggregate` in method [[ParStringCombiner.result()]]
 */
class ParString(val str: String) extends parallel.immutable.ParSeq[Char]{
  override def apply(i: Int): Char = str.charAt(i)

  override protected[parallel] def splitter: SeqSplitter[Char] = ParStringSplitter(str, 0, length)

  override def length: Int = str.length

  /**
   * From the book: Finally, parallel collections need a seq method, which returns a sequential Scala collection.
   * Since String itself comes from Java and is not a Scala collection, we will use [[scala.collection.immutable.WrappedString]]
   * wrapper class from the Scala collections library
   * @return
   */
  override def seq = new collection.immutable.WrappedString(str)

  override def newCombiner: Combiner[Char, ParSeq[Char]] = new ParStringCombiner
}
