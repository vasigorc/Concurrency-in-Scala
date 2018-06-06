package org.learningconcurrency

class Exercises02Spec extends BaseSpec{

  import Exercises02._

  "parallel" should "return a tuple with the result values of passed by-name parameters" in{
    val bynameA = math.random()
    val bynameB = math.random()
    parallel(bynameA, bynameB) should equal((bynameA, bynameB))
  }
}
