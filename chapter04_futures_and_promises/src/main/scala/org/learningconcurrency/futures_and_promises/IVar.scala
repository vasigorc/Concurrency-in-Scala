package org.learningconcurrency.futures_and_promises

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Try

class IVar[T] {
  self =>

  import IVar.VALUE_NOT_SET

  lazy private val p = Promise[T]

  def apply: T = {
    if (p.isCompleted)
      Await.result(p.future, 1 second)
    else
      throw new Exception(VALUE_NOT_SET)
  }

  def :=(x: T): Unit = {
    if (!p.tryComplete(Try {x}))
      throw new Exception(s"Impossible to assign $x - this object has already been assigned value: ${self.apply}")
  }
}

object IVar {
  val VALUE_NOT_SET = s"Value is not set yet"
}
