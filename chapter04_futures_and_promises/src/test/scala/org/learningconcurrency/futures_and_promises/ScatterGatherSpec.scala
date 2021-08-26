package org.learningconcurrency.futures_and_promises

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class ScatterGatherSpec extends AsyncFlatSpec with Matchers {

  behavior of "scatterGather"

  it should "return a Future of a Seq of parallel processed values " +
    "resulting from the passedin callables" in {
      val result: Future[Seq[Int]] = scatterGather(Seq(() => 1, () => 2, () => 3, () => 4))
      result map { seq => seq should contain allOf (1, 2, 3, 4) }
    }
}