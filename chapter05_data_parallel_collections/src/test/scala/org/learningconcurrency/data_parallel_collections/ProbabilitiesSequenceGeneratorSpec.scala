package org.learningconcurrency.data_parallel_collections

import org.learningconcurrency.BaseSpec
import org.scalatest.PrivateMethodTester

class ProbabilitiesSequenceGeneratorSpec extends BaseSpec with PrivateMethodTester {


  behavior of "approximateProbability"

  private lazy val approximateProbabilityValue = PrivateMethod[Double]('approximateProbability)

  it should "return an approximate value within a range of passed-in value " +
    "and the passed-in value -0.2" in {
    val roughProbability = 1D
    val approximatedProbability: Double = ProbabilitiesSequenceGenerator invokePrivate approximateProbabilityValue(roughProbability)

    approximatedProbability should (be < roughProbability and be > 0.8)
  }

  it should "throw an Exception when being passed-in a value superior to 1" in {
    an[IllegalArgumentException] should be thrownBy (ProbabilitiesSequenceGenerator invokePrivate approximateProbabilityValue(2D))
  }


  behavior of "generateEvenProbabilities"

  it should "throw an Exception when being asked for 0 probabilities" in {
    an[IllegalArgumentException] should be thrownBy ProbabilitiesSequenceGenerator.generateEvenProbabilities(0)
  }

  it should "return a list of sequences equal to the passed-in parameter" in {
    val result = ProbabilitiesSequenceGenerator.generateEvenProbabilities(7)
    result.size shouldEqual(7)
  }

  it should "yield a sorted list" in {
    ProbabilitiesSequenceGenerator.generateEvenProbabilities(8) shouldBe sorted
  }
}