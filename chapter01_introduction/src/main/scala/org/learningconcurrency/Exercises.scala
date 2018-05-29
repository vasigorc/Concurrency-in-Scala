package org.learningconcurrency

import scala.util.Try

object Exercises {

  def compose[A,B,C](g: B => C, f: A => B): A => C = f.andThen(g)

  def fuse[A, B](a: Option[A], b: Option[B]):Option[(A, B)] = (a,b) match {
    case (Some(val1), Some(val2)) => Some(val1, val2)
    case (_, _) => None
  }

  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = {
    Try{xs forall(pred)} getOrElse(false)
  }

  case class Pair[P,Q](val first: P, val second: Q)
}
