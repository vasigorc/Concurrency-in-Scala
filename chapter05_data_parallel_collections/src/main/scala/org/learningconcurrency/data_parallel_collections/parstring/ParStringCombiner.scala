package org.learningconcurrency.data_parallel_collections.parstring

import org.learningconcurrency.data_parallel_collections.parstring.ParStringCombiner._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.CollectionConverters.MutableArrayBufferIsParallelizable
import scala.collection.parallel.Combiner

class ParStringCombiner(resultImpl: ArrayBuffer[mutable.StringBuilder] => ParString = orginalResultImpl) extends Combiner[Char, ParString] {
  private val chunks = new ArrayBuffer += new mutable.StringBuilder
  private var lastChunk = chunks.last
  private var sz = 0

  override def combine[N <: Char, NewTo >: ParString](other: Combiner[N, NewTo]): Combiner[N, NewTo] = {
    if (this eq other) this else other match {
      case other: ParStringCombiner =>
        sz += other.size
        chunks ++= other.chunks
        lastChunk = chunks.last
        this
    }
  }

  override def size: Int = sz

  override def addOne(elem: Char): ParStringCombiner.this.type = {
    lastChunk += elem
    sz += 1
    this
  }

  override def clear(): Unit = {
    chunks.clear()
    chunks += new mutable.StringBuilder
    lastChunk = chunks.last
    sz = 0
  }

  override def result(): ParString = resultImpl(chunks)
}

object ParStringCombiner {

  // result implementation from the book
  def orginalResultImpl: ArrayBuffer[StringBuilder] => ParString = (chunks: ArrayBuffer[StringBuilder]) => {
    val rsb = new mutable.StringBuilder
    for (sb <- chunks) rsb.append(sb)
    new ParString((rsb.toString()))
  }

  def parResultImpl: ArrayBuffer[mutable.StringBuilder] => ParString = (chunks: ArrayBuffer[mutable.StringBuilder]) => {
    val sb: mutable.StringBuilder = chunks.par.aggregate(new mutable.StringBuilder)((acc, sb) => acc.append(sb), (s1, s2) => s1.append(s2))
    new ParString(sb.result())
  }
}