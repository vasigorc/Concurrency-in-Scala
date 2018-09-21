package org

import java.time.LocalTime

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext
import scala.util.Random
import scala.util.control.NonFatal

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

  def execute(body: => Unit) = ExecutionContext.global.execute(
    new Runnable {
      override def run(): Unit = body
    }
  )

  def closeAndAddSuppressed[T <: AutoCloseable](exception: Throwable, resource: T): Unit = {
    if(exception != null){
      try{
        resource.close()
      } catch {
        case NonFatal(suppressed) =>
          exception.addSuppressed(suppressed)
      }
    } else {
      resource.close()
    }
  }

  //from https://medium.com/@dkomanov/scala-try-with-resources-735baad0fd7d
  def withRessources[T <: AutoCloseable, V](r: => T)(f: T => V): V = {
    val resource: T = r
    require(resource != null, "resource is null")
    var exception: Throwable = null
    try {
      f(resource)
    } catch {
      case NonFatal(e) =>
        exception = e
        throw e
    } finally {
      closeAndAddSuppressed(exception, resource)
    }
  }
}