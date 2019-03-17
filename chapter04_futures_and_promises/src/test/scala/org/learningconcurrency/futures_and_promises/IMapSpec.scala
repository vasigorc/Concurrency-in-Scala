package org.learningconcurrency.futures_and_promises

import org.scalatest.{AsyncFlatSpec, Matchers}

class IMapSpec extends AsyncFlatSpec with Matchers {

  def fixture = new {
    val instance = new IMap[Int, String]
  }

  behavior of "update"

  it should "throw an Exception when called with same key more then once " in {
    val iMap = fixture.instance
    iMap update(7, "Seven")
    an [IllegalArgumentException] should be thrownBy iMap.update(7, "Sieben")
  }
}