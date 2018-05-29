package org.learningconcurrency

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.{Inside, Matchers, PropSpec}
import org.scalatest.prop.TableDrivenPropertyChecks

class ExercisesPropSpec extends PropSpec with TableDrivenPropertyChecks with Matchers with Inside{

  import Exercises._

  lazy val genPairs = for {
    left <- Gen.listOfN(10, Gen.alphaChar)
    right <- Gen.listOfN(10, Gen.alphaChar)
  } yield (left.mkString, right.mkString)

  property("pattern match tuples of alphanum to our custom pair class"){
    forAll(genPairs) {(l: String, r: String) =>
      val pair = Pair(l, r)
      inside(pair) { case Pair(firstName, lastName) =>
        firstName shouldEqual(l)
        lastName shouldEqual(r)
      }
    }
  }
}
