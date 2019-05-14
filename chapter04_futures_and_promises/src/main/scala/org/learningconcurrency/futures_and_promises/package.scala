package org.learningconcurrency

import java.util.concurrent.TimeoutException
import java.util.{ Timer, TimerTask }

import scala.concurrent.{ Future, Promise }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import scala.async.Async.{ async, await }

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

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete (x => p tryComplete x)
      that onComplete (y => p tryComplete y)
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
        case Success(_) => that onComplete (z => p tryComplete z)
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
        case Success(s) => Future { p(s) }
        case Failure(_) => Future { false }
      }
    }

    /*
      Exercise 4 Repeat Exercise 3, but use Promise objects instead of future combinations
     */
    def existsWithPromise(p: T => Boolean): Future[Boolean] = {
      val promisedBoolean = Promise[Boolean]
      self onComplete {
        case Success(s) => promisedBoolean success p(s)
        case Failure(_) => promisedBoolean success false
      }
      promisedBoolean future
    }

    /*
      Exercise 5 Repeat Exercise 3, but use Scala Async framework
     */
    def existsAsync(p: T => Boolean): Future[Boolean] = async {
      p(await(self))
    }.recoverWith { case _ => Future { false } }

  }

  implicit class PromiseOps[T](val self: Promise[T]) {

    /**
     * Exercise 8. Extend the type Promise[T] with the compose method, which takes a
     * function of the type S => T, and returns a Promise[S] object.
     * Whenever the resulting promise is completed with some value of x
     * of the type (or failed), the original promise must be completed
     * with the value f(x) asynchrounously (or failed), unless the original promise
     * is already completed
     */
    def compose[S](f: S => T): Promise[S] = {
      val p = Promise[S]()
      p.future onComplete {
        case Success(x) => if (!self.isCompleted) self.success(f(x))
        case Failure(exc) => if (!self.isCompleted) self.failure(exc)
      }
      p
    }
  }
  /*
      Exercise 6 Implement the spawn method, which takes a command line String,
      asynchronously executes it as a child process, and returns a future with
      the exit code of the child process. Make sure your implementation
      does not cause thread starvation
     */
  def spawn(command: String): Future[Int] = {
    import scala.sys.process._

    async(command.!)
  }

  /**
   * Exercise 9. Implement the [[scatterGather]] method, which given a sequence of tasks,
   * runs those tasks as parallel asynchronous computations, then combines the results,
   * and returns a future that contains the sequence of results from different tasks.
   * @param tasks - passed-in Sequence of callables
   * @return future of values returned from tasks
   */
  def scatterGather[T](tasks: Seq[() => T]): Future[Seq[T]] = {
    val seqOfFutures: Seq[Future[T]] = tasks map (call => async(call.apply()))
    Future.sequence(seqOfFutures)
  }
}