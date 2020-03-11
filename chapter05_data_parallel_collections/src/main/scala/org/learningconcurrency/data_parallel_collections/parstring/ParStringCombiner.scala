package org.learningconcurrency.data_parallel_collections.parstring

import org.learningconcurrency.data_parallel_collections.parstring.ParStringCombiner._

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.Combiner

class ParStringCombiner(resultImpl: ArrayBuffer[StringBuilder] => ParString = orginalResultImpl) extends Combiner[Char, ParString] {
  private val chunks = new ArrayBuffer += new StringBuilder
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

  override def +=(elem: Char): ParStringCombiner.this.type = {
    lastChunk += elem
    sz += 1
    this
  }

  override def clear(): Unit = {
    chunks.clear()
    chunks += new StringBuilder
    lastChunk = chunks.last
    sz = 0
  }

  override def result(): ParString = resultImpl(chunks)
}

object ParStringCombiner {

  // result implementation from the book
  def orginalResultImpl: ArrayBuffer[StringBuilder] => ParString = (chunks: ArrayBuffer[StringBuilder]) => {
    val rsb = new StringBuilder
    for (sb <- chunks) rsb.append(sb)
    new ParString((rsb.toString()))
  }

  def parResultImpl: ArrayBuffer[StringBuilder] => ParString = (chunks: ArrayBuffer[StringBuilder]) => {
    val sb: StringBuilder = chunks.par.aggregate(new StringBuilder)((acc, sb) => acc.append(sb), (s1, s2) => s1.append(s2))
    new ParString(sb.result())
  }
}