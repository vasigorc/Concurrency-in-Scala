package org.learningconcurrency.data_parallel_collections

import org.apache.commons.lang3.RandomStringUtils

import scala.language.postfixOps
import scala.util.Random

/**
  * Exercise 2 Count the occurrences of the whitespace character in a randomly
  * generated string, where the probability of a whitespace at each position is
  * determined by a p parameter. Use the parallel foreach method. Plot a graph
  * that correlates the running time of this operation with the p parameter
  */
object Ex02_CountWhitespacesPar extends App with RandomWhiteSpacedString {

  import ProbabilitiesSequenceGenerator._

  private val random200: Double => String = randomWhitespacedString(randomStringOf(200))

  private def calculateGraph(probabilities: Seq[Double], pToString: Double => String): Vector[(Double, Double)] = {

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

  val probabilities: Seq[Double] = generateEvenProbabilities(10)

  calculateGraph(probabilities, random200) foreach {
    case (p, runningTime) => println(s"For p $p the time is $runningTime")
  }
}

trait RandomWhiteSpacedString {

  protected def randomStringOf(length: Int): String = RandomStringUtils.randomAlphabetic(length)

  protected def randomWhitespacedString(targetString: String): Double => String = (p: Double) => {
    require(p > 0 && p < 1)

    def randomWhiteSpace(c: Char): Char = c match {
      case _ if Random.nextDouble() <= p => ' '
      case _ => c
    }

    targetString map (randomWhiteSpace(_))
  }
}

object ProbabilitiesSequenceGenerator {

  private lazy val INACCURACY_FACTOR = 0.2

  //instead of 1 (for example) we want a random value between 0.8 and 1
  private def approximateProbability(roughProbability: Double): Double = {
    require(roughProbability <= 1, "Probability can not be greater then 1")

    val minRange = if (roughProbability < INACCURACY_FACTOR) 0 else roughProbability - INACCURACY_FACTOR

    minRange + (roughProbability - minRange) * Random.nextDouble()
  }

  //produces ~ evenly distributed sequence of probabilities like 0.18, 0.38, 0.58, 0.78, 0.98
  def generateEvenProbabilities(n: Int): Seq[Double] = {
    require(n > 0, "# of requested probabilities must be greater then zero ")

    val stepIncrement = 1D / n
    (1 to n map (step => (approximateProbability(step * stepIncrement.doubleValue()))) toList) sorted
  }
}