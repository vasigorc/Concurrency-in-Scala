package org.learningconcurrency.futures_and_promises

import org.scalatest.AsyncFunSpec

import scala.concurrent.Future

class FutureOpsSpec extends AsyncFunSpec {

  val isEven: Int => Boolean = i => i % 2 == 0

  describe("exists") {
    it("will yield false when upstream future throws an exception") {
      val failedFuture: Future[Int] = Future.failed(new RuntimeException)
      failedFuture exists(isEven) map(result => assert(!result))
    }

    it("will yield true when the provided predicate evaluate") {
      val successfulFuture = Future.successful(10)
      successfulFuture exists(isEven) map(assert(_))
    }

    it("will yield false when the provided predicate doesn't evaluate") {
      val unsuccessfulFuture = Future.successful(7)
      unsuccessfulFuture exists(isEven) map(result => assert(!result))
    }
  }

  describe("existsWithPromise") {
    it("will yield false when upstream future throws an exception") {
      val failedFuture: Future[Int] = Future.failed(new RuntimeException)
      failedFuture existsWithPromise (isEven) map(result => assert(!result))
    }

    it("will yield true when the provided predicate evaluate") {
      val successfulFuture = Future.successful(10)
      successfulFuture existsWithPromise (isEven) map(assert(_))
    }

    it("will yield false when the provided predicate doesn't evaluate") {
      val unsuccessfulFuture = Future.successful(7)
      unsuccessfulFuture existsWithPromise (isEven) map(result => assert(!result))
    }
  }
}