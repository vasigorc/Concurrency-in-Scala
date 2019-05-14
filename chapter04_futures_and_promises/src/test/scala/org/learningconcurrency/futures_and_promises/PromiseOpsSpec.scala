package org.learningconcurrency.futures_and_promises

import org.scalatest.AsyncFreeSpec
import org.scalatest.Matchers
import scala.concurrent.Promise
import scala.concurrent.Future

class PromiseOpsSpec extends AsyncFreeSpec with Matchers {

  "When calling compose with a function f as param on an original Promise" - {
    "and the returned Promise is completed" - {
      "then the original Promise should be completed with function f applied " +
        "to the value of the composed Promise" in {
          val original: Promise[Int] = Promise[Int]
          val composed: Promise[String] = original.compose((x: String) => x.toInt)
          composed.success("1")
          original.future map { value => assert(value == 1) }
        }
    }

    "and the returned Promise throws an exception" - {
      "then trying to complete the original Promise should " +
        "result in an exception" in {
          val original: Promise[Int] = Promise[Int]
          val composed: Promise[String] = original.compose((x: String) => x.toInt)
          composed.failure(new RuntimeException("Something went wrong here"))
          recoverToSucceededIf[RuntimeException] {
            Future { original.success(2) }
          }
        }
    }

    "and the original Promise is completed" - {
      "then completing the Promise reutrned by compose method " +
        "doesn't change the value of the original Promise" in {
          val original: Promise[Int] = Promise[Int]
          val composed: Promise[String] = original.compose((x: String) => x.toInt)
          original.success(2)
          composed.success("1")
          original.future map { value => assert(value == 2) }
        }
    }
  }
}