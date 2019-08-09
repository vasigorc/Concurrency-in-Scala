package org.learningconcurrency.data_parallel_collections

import org.apache.commons.lang3.RandomStringUtils

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.util.Random

/**
  * Exercise 2 Count the occurrences of the whitespace character in a randomly
  * generated string, where the probability of a whitespace at each position is
  * determined by a p parameter. Use the parallel foreach method. Plot a graph
  * that correlates the running time of this operation with the p parameter
  */
trait Ex02_CountWhitespacesPar extends RandomWhiteSpacedString {

  import ProbabilitiesSequenceGenerator._
  val stringLength: Int = 200
  val nrProbabilities: Int = 10

  /**
    * function that takes a double and uses it as a probability to replace each character
    * in a string of length [[stringLength]] with a white space
    */
  protected val stringToStringWithWhiteSpaces: Double => String = randomWhitespacedString(randomStringOf(stringLength))

  protected def calculateGraph(probabilities: Seq[Double], pToString: Double => String): Vector[(Double, Double)] = {

    @tailrec
    def helper(accumulator: Vector[(Double, Double)], remainingProps: Seq[Double]): Vector[(Double, Double)] = {
      if (remainingProps.isEmpty) return accumulator
      val head = remainingProps.head
      val stringWithWhitespaces = pToString(head)
      val actualNrWhitespaces = getTimedResult(stringWithWhitespaces.par.count(_ == ' ')).time
      //append new (p, runningTime) to the end of the vector
      helper(accumulator :+ (head, actualNrWhitespaces), remainingProps.tail)
    }

    helper(Vector[(Double, Double)](), probabilities)
  }

  val probabilities: Seq[Double] = generateEvenProbabilities(nrProbabilities)
}

trait RandomWhiteSpacedString {

  protected def randomStringOf(length: Int): String = RandomStringUtils.randomAlphabetic(length)

  /**
    * Replace each character in the passed-in String with a white space with probability of p
    * @param targetString - passed-in string
    * @return result of the above mentioned function
    */
  protected def randomWhitespacedString(targetString: String): Double => String = (p: Double) => {
    require(p > 0 && p < 1)

    def randomWhiteSpace(c: Char): Char = c match {
      case _ if Random.nextDouble() <= p => ' '
      case _ => c
    }

    targetString map randomWhiteSpace
  }
}

object ProbabilitiesSequenceGenerator {

  //instead of 1 (for example) we want a random value between 0.8 and 1
  private def approximateProbability(stepIncrement: Double, roughProbability: Double): Double = {
    require(roughProbability <= 1, "Probability can not be greater then 1")

    val inaccuracyFactor = stepIncrement / 2
    val minRange = if (roughProbability < inaccuracyFactor) 0 else roughProbability - inaccuracyFactor

    minRange + (roughProbability - minRange) * Random.nextDouble()
  }

  //produces ~ evenly distributed sequence of probabilities like 0.18, 0.38, 0.58, 0.78, 0.98
  def generateEvenProbabilities(n: Int): Seq[Double] = {
    require(n > 0, "# of requested probabilities must be greater then zero ")

    val stepIncrement = 1D / n
    (1 to n map (step => approximateProbability(stepIncrement, step * stepIncrement)) toList) sorted
  }
}