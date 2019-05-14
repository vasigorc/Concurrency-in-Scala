package org.learningconcurrency.futures_and_promises

import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.concurrent.duration._

/**
  * Exercise 11 DAG is a directed acyclic graph
  * Your task is to implement the fold method that takes a DAG node and a function that maps each item and its inputs
  * into some value, and then returns the future with the resulting value of the input node
  *
  * The fold method runs an asynchronous task for each item in the DAG to map the item and its inputs to a new value.
  * Dependencies between DAG items must be respected; an item can only run after all of its dependencies have been
  * computed.
  *
  * @param value - immutable property held by each DAG
  * @tparam T - type of value and of edges
  */
class DAG[T](val value: T) {
  val edges: mutable.Set[DAG[T]] = scala.collection.mutable.Set[DAG[T]]()

  def fold[T1 >: T](z: T1)(op: (T1, T1) => T1): Future[T1] = {
    val eventualValue: Future[T1] = Future { op(z, value) }
    if (edges.nonEmpty) {
      val eventualInputValues: Seq[Future[T1]] = edges.map(dag => dag.fold(z)(op)).toSeq
      Future.sequence(eventualValue +: eventualInputValues) map { s => (z /: s) (op) }
    } else eventualValue
  }
}

object DAG extends App{
  /**
    *
    * @param g - root DAG
    * @param f - function that will run a sequence of jobs against each DAG.val
    * @tparam T - type of the enclosed DAG values and edges
    * @tparam S - resulting type of the fold function
    * @return Future value the result of root job
    */
  def fold[T, S](g: DAG[T], f: (T, Seq[S]) => S): Future[S] = {
    val inputsFuturesSet: mutable.Set[Future[S]] = g.edges map (fold(_, f))
    val futureInputs: Future[mutable.Set[S]] = Future sequence inputsFuturesSet
    futureInputs map (set => f(g.value, set.toSeq))
  }

  private val a = new DAG("a")
  private val b = new DAG("b")
  private val c = new DAG("c")
  private val d = new DAG("d")
  private val e = new DAG("e")
  a.edges += b
  b.edges += c
  b.edges += d
  c.edges += e
  d.edges += e
  private val result: Future[String] = fold(a, (first: String, seq: Seq[String]) => (first+:seq).mkString(", "))
  println(Await.result(result, 10.seconds))
}