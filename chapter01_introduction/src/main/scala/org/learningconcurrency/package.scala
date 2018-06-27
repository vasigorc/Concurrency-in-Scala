package org

import java.time.LocalTime

import scala.annotation.tailrec
import scala.util.Random

package object learningconcurrency {

  def log(msg: String): Unit = println(s"${Thread.currentThread.getName}: $msg") //from chapter 1

  def thread(body: => Unit): Thread = {
    val t = new Thread{
      override def run(): Unit = body
    }
    t.start()
    t
  }

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
  def combinationsResult(seqSize: Int, dimension: Int): Int = {
    factorial(seqSize) / factorial(dimension) * factorial(seqSize-dimension)
  }

  //Write a program that generates all permutations of n different objects
  def permutations[T]:List[T]=>Traversable[List[T]] = {
    case Nil => List(Nil)
    case xs => {
      for {
        (x,i) <-xs.zipWithIndex
        ys <- permutations(xs.take(i) ++ xs.drop(1 + i))
      } yield {
        x :: ys
      }
    }
  }

  def randomTime:LocalTime = LocalTime.of(16+Random.nextInt(4), Random.nextInt(60)) //hour is supposedly within 4 hours after 16
}
