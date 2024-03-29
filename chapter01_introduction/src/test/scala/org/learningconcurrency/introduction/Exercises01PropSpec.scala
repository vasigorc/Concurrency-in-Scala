package org.learningconcurrency.introduction

import org.learningconcurrency.combinationsResult
import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class Exercises01PropSpec extends AnyPropSpec with Matchers with Inside {

  import org.learningconcurrency.introduction.Exercises01._

  def fixture = new {
    val tenInts = Seq[Int](1,4,9,16,25,34,7,10,8,11)
  }

  lazy val genPairs = for {
    left <- Gen.listOfN(10, Gen.alphaChar)
    right <- Gen.listOfN(10, Gen.alphaChar)
  } yield (left.mkString, right.mkString)

  property("pattern match tuples of alphanumerics to our custom pair class"){
    forAll(genPairs) {case (left: String, right: String) =>
      val pair = Pair(left, right)
      inside(pair) { case Pair(firstName, lastName) =>
        firstName == left && lastName == right
      }
    }
  }

  property("number of combinations should conform to formula"){
    val dimensions: Gen[Int] = Gen.chooseNum(1, 4, 0) //between 1 and 4 with special case 0
    val sequence = fixture.tenInts
    import vkostyukov.CombinatorialOps._
    forAll(dimensions)((dim:Int) => sequence.toList.xcombinations(dim).size == combinationsResult(sequence.size, dim))
  }
}
