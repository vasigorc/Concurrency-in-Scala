package org.learningconcurrency

class ExercisesSpec extends BaseSpec {

  import Exercises._

  "One plus one times four" should "equal 8" in {
    compose[Int, Int, Int](_*4, _+1)(1) shouldEqual(8)
  }

  "fuse " should "return None if at least one absent passed to it" in {
    fuse(Some(1), None) shouldEqual(None)
  }

  "fuse" should "yield a pair of values when both are present" in {
    fuse(Some("Hello"), Some("World")) shouldEqual(Some("Hello", "World"))
  }

  "check" should "return true when testing prime numbers" in {
    assert(check(List(1,3,5,7,11))(_ % 2 != 0))
  }
}
