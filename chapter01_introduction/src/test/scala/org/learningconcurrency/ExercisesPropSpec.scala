package org.learningconcurrency

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest.prop._
import org.scalatest.{Inside, Matchers, PropSpec}

class ExercisesPropSpec extends PropSpec with Checkers with Matchers with Inside{

  import Exercises._

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
}
