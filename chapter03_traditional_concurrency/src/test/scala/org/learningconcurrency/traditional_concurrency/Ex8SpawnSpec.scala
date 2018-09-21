package org.learningconcurrency.traditional_concurrency

import org.learningconcurrency.BaseSpec

class Ex8SpawnSpec extends BaseSpec{

  behavior of "Exercise 8 spawn"

  "1 + 1" should "be correctly evaluated in a different Process" in {
    spawn(1 + 1) shouldEqual 2
  }
}
