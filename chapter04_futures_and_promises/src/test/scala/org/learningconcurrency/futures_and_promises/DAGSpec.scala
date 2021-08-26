package org.learningconcurrency.futures_and_promises

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class DAGSpec extends AsyncFlatSpec with Matchers {

  behavior of "fold"

  it should "map the dependents first and then the root value" in {
    val a = new DAG("a")
    val b = new DAG("b")
    val c = new DAG("c")
    val d = new DAG("d")
    val e = new DAG("e")
    a.edges += b
    b.edges += c
    b.edges += d
    c.edges += e
    d.edges += e
    val eventualString: Future[String] = a.fold("")((acc, currentValue) => acc + currentValue)
    eventualString map (result => result shouldEqual "abcede")
  }
}