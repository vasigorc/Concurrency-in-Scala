package org.learningconcurrency.data_parallel_collections

import org.learningconcurrency.BaseSpec
import org.scalatest.PrivateMethodTester

class ProbabilitiesSequenceGeneratorSpec extends BaseSpec with PrivateMethodTester {

  private lazy val approximateProbabilityValue = PrivateMethod[Double]('approximateProbability)

  behavior of "ProbabilitiesSequenceGeneratorSpec"

  "approximateProbability" should "return an approximate value within a range of passed-in value " +
    "and the passed-in value -0.2" in {
    val roughProbability = 1D
    val approximatedProbability: Double = ProbabilitiesSequenceGenerator invokePrivate approximateProbabilityValue(roughProbability)

    approximatedProbability should (be < roughProbability and be > 0.8)
  }

  "approximateProbability" should "throw an Exception when being passed-in a value superior to 1" in {
    an[IllegalArgumentException] should be thrownBy (ProbabilitiesSequenceGenerator invokePrivate approximateProbabilityValue(2D))
  }
}
