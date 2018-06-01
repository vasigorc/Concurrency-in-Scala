package org.learningconcurrency

import scala.util.Try
import scala.util.matching.Regex

object Exercises {

  def compose[A,B,C](g: B => C, f: A => B): A => C = f.andThen(g) //ex 1

  def fuse[A, B](a: Option[A], b: Option[B]):Option[(A, B)] = (a,b) match { //ex 2
    case (Some(val1), Some(val2)) => Some(val1, val2)
    case (_, _) => None
  }

  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = { //ex 3
    Try{xs forall pred} getOrElse false
  }

  case class Pair[P,Q](first: P, second: Q) //ex 4

  def permutations(x: String): Seq[String] = x.permutations.toSeq //ex 5

  def permutationsByHand(x: String): Seq[String] = { // ex 5

    def internalLoop(text: Seq[Char]):Seq[String] = text match {
      case Nil => Seq()
      case Seq(head) => Seq(head.toString)
        //flatMap to get a Seq[String] instead of Seq[Seq[String]]
      case xs => xs.flatMap(nextChar => internalLoop(xs.diff(Seq(nextChar)))
        .map(xsDiff=>{println(nextChar+:xsDiff);nextChar+:xsDiff}))
    }

    internalLoop(x.toSeq)
  }

  def combinations(n: Int, xs: Seq[Int]):Iterator[Seq[Int]] = xs.combinations(n)  // ex 6

  def combinationsByHand(n: Int, xs: Seq[Int]):Iterator[Seq[Int]] = { // ex 6
    n match {
      case 0 => Iterator()
      case 1 => Iterator(xs)
      case _ => {
        val lesser = combinationsByHand(n-1, xs)
        lesser.flatMap(seq=> lesser.map(seq1=>seq1++seq))
      }
    }
  }

  def matcher(regex: String):PartialFunction[String, List[String]] = new PartialFunction[String, List[String]] { // ex 7
    override def isDefinedAt(x: String): Boolean = !apply(x).isEmpty

    override def apply(v1: String): List[String] = {
      val reg = new Regex(regex)
      reg.findAllIn(v1).toList
    }
  }
}
