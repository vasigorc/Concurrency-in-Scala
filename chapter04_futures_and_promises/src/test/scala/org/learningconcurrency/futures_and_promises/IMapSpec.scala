package org.learningconcurrency.futures_and_promises

import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.Future

class IMapSpec extends AsyncFlatSpec with Matchers {

  def fixture: Object {
    val instance: IMap[Int, String]
  } = new {
    val instance = new IMap[Int, String]
    val std_key = 7
  }

  behavior of "update"

  it should "throw an Exception when called with same key more then once " in {
    val iMap = fixture.instance
    iMap update(7, "Sieben")
    an [IllegalArgumentException] should be thrownBy iMap.update(7, "Seven")
  }

  behavior of "apply"

  it should "return an uncompleted Future for an empty Map" in {
    fixture.instance.apply(7).value shouldEqual None
  }

  it should "return a Future for a key that will only complete when that key is added" in {
    val iMap = fixture.instance
    val eventualSeven: Future[String] = iMap.apply(7)

    assert(!eventualSeven.isCompleted)

    iMap.update(7, "Sieben")
    eventualSeven.map(value => value shouldEqual "Sieben")
  }
}