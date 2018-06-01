package org

import scala.annotation.tailrec

package object learningconcurrency {

  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg")

  def factorial(n: Int): Int = {
    @tailrec
    def iter(x: Int, result: Int): Int =
      if (x ==0) result
      else iter(x -1, result * x)

    iter(n,1)
  }

  /**
    * should be seqSize! / dimension! * (sezSize-dimension)!
    * @param seqSize
    * @param dimension
    */
  def combinationsResult(seqSize: Int, dimension: Int) = {
    factorial(seqSize) / factorial(dimension) * factorial(seqSize-dimension)
  }
}
