package org.learningconcurrency

import java.util.concurrent.TimeoutException
import java.util.{Timer, TimerTask}

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

package object futures_and_promises {

  private val timer = new Timer(true)

  def timeout[T](t: Long): Future[Either[Throwable, T]] = {
    val p = Promise[Either[TimeoutException, T]]
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        p success Left(new TimeoutException())
        timer.cancel()
      }
    }, t)
    p.future
  }

  implicit class FutureOps[T] (val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete { case x => p tryComplete x}
      that onComplete { case y => p tryComplete y}
      p.future
    }

    /**
      * Success value of the initial future is discarded
      * @param that - the other future
      * @return success value of the other future or failure (either first or second)
      */
    def and[R](that: Future[R]): Future[R] = {
      val p = Promise[R]
      self andThen {
        case Success(_) => that onComplete { case z => p tryComplete z }
        case Failure(ex) => p failure ex
      }
      p future
    }

    /**
      * Exercise 3 Expand the Future[T] type with the exists method, which takes a predicate
      * and returns a Future[Boolean]
      * @param p predicate function
      * @return the resulting future is completed with true if and only if the original future is completed and the
      * predicate returns true, and false otherwise. You can use future combinations, but you are not allowed
      * to create any Promise objects in the implementation.
      */
    def exists(p: T => Boolean): Future[Boolean] = {
      self transformWith {
        case Success(s) => Future {
          if (p(s)) true else false
        }
        case Failure(_) => Future {
          false
        }
      }
    }
  }
}
