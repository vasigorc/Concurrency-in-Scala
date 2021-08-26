package org.learningconcurrency.data_parallel_collections.parstring

import scala.collection.parallel.SeqSplitter


/**
 * Is invalidated after calling the [[split]] or [[psplit()]] method,
 * so we are allowed to mutate its [[i]] field.
 *
 * @param str
 * @param i
 * @param limit
 */
case class ParStringSplitter(str: String, var i: Int, limit: Int) extends SeqSplitter[Char] {
  // creates a new parallel string splitter using the state of the current splitter
  override def dup: SeqSplitter[Char] = new ParStringSplitter(str, i, limit)

  /**
   * If there is more than one element remaining, we call the [[psplit()]] method.
   * Otherwise, if there are no elements to split, we return the this splitter.
   * @return
   */
  override def split: Seq[SeqSplitter[Char]] = {
    val rem = remaining
    if (rem >= 2) psplit(rem / 2, rem - rem / 2)
    else Seq(this)
  }

  /**
   * Increments the i variable and creating a new splitter for each size sz in the sizes parameter.
   * @param sizes - used to peel off parts of the original splitter.
   * @return
   */
  override def psplit(sizes: Int*): Seq[SeqSplitter[Char]] = {
    val ss = for (sz <- sizes) yield {
      val newLimit = (i + sz) min limit
      val ps = new ParStringSplitter(str, i, newLimit)
      i = newLimit
      ps
    }

    if (i == limit) ss
    else ss :+ new ParStringSplitter(str, i, limit)
  }

  // uses limit and i to compute the number of remaining elements
  override def remaining: Int = limit - i

  override def hasNext: Boolean = i < limit

  override def next(): Char = {
    val r = str.charAt(i)
    i += 1
    r
  }
}
