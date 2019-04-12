package org.learningconcurrency.futures_and_promises

import org.scalatest.AsyncFreeSpec
import org.scalatest.Matchers
import scala.concurrent.Promise

class PromiseOpsSpec extends AsyncFreeSpec with Matchers {

  import org.learningconcurrency.futures_and_promises._
  
  "When invoking compose with a function f as param on an original Promise" - {
    "and the returned Promise is completed" - {
      "then the original Promise should be completed with function f applied" in {
        val original: Promise[Int] = Promise[Int]
        val composed: Promise[String] = original.compose((x: String) => x.toInt)
        composed.success("1")
        original.future map { value => assert(value == 1) }
      }
    }
  }
}