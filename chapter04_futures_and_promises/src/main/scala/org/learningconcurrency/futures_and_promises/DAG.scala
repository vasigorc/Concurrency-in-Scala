package org.learningconcurrency.futures_and_promises

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Exercise 11 DAG is a directed acyclic graph
  * Your task is to implement the fold method that takes a DAG node and a function that maps each item and its inputs
  * into some value, and then returns the future with the resulting value of the input node
  *
  * The fold method runs an asynchronous task for each item in the DAG to map the item and its inputs to a new value.
  * Dependencies between DAG items must be respected; an item can only run after all of its dependencies have been
  * computed.
  *
  * @param value
  * @tparam T
  */
class DAG[T: Ordering](val value: T) {
  val edges = scala.collection.mutable.Set[DAG[T]]()

  def fold[T1 >: T](z: T1)(op: (T1, T1) => T1): Future[T1] = {
    val eventualValue: Future[T1] = Future { op(z, value) }
    if (edges.nonEmpty) {
      val eventualInputValues: mutable.Set[Future[T1]] = edges.map(_ => fold(z)(op)) += eventualValue
      Future.sequence(eventualInputValues) map { s => (z /: s) (op) }
    } else eventualValue
  }
}