package org.learningconcurrency.introduction

import org.learningconcurrency.BaseSpec

class Exercises01Spec extends BaseSpec {

  import org.learningconcurrency.introduction.Exercises01._

  "One plus one times four" should "equal 8" in {
    compose[Int, Int, Int](_*4, _+1)(1) shouldEqual(8)
  }

  "fuse " should "return None if at least one absent passed to it" in {
    fuse(Some(1), None) shouldEqual(None)
  }

  "fuse" should "yield a pair of values when both are present" in {
    fuse(Some("Hello"), Some("World")) shouldEqual Some("Hello", "World")
  }

  "check" should "return true when testing prime numbers" in {
    assert(check(List(1,3,5,7,11))(_ % 2 != 0))
  }

  "'abc' string" should "have a total of six permutations" in {
    permutationsByHand("abc").size shouldEqual(permutations("abc") size)
  }

  "comninations of 1 from a Seq of single element" should "return 1" in {
    import vkostyukov.CombinatorialOps._
    Seq(1).toList.xcombinations(1).size shouldEqual(1)
  }

  "combinations of 2 from a Seq of 4" should "be 6" in {
    import vkostyukov.CombinatorialOps._
    List(1,2,3,4).xcombinations(2).size shouldEqual(6)
  }

  "\"hathatthattthatttt\"" should "yield two matches for regex \"hat[^a]+\"" in {
    matcher("hat[^a]+")("hathatthattthatttt").size shouldEqual(2)
  }
}
