package org.learningconcurrency.futures_and_promises

import java.util.concurrent.ConcurrentHashMap

import rx.lang.scala.observables.ConnectableObservable
import rx.lang.scala.subjects.PublishSubject
import scala.collection.JavaConverters._

import scala.concurrent.{Future, Promise}
import scala.language.postfixOps

/**
  * Exercise 7. Implement the IMap class, which represents a single-assignment map.
  * Pairs of keys and values can be added to the IMap object, but they can never be removed or modified.
  * In addition to futures and promises, you may use [[scala.collection.concurrent.Map]]
  *
  * @tparam K - key type
  * @tparam V - key value
  */
class IMap[K, V] extends IMapContract[K, V] {

  /*
    A Subject is an Observer and an Observable at the same time. Events are received by calling the onNext
    on it. These are eagerly being pushed to all subscribers. Subscriber keeps track of those events.
   */
  private val publisher = PublishSubject[K]()
  /*
      Coordinates multiple subscribers. Ensures that there exists at most one Subscriber at
      all times, but in reality there can be many of them sharing the same underlying resource
   */
  private val connectableObservable: ConnectableObservable[K] = publisher.publish
  connectableObservable.connect
  private val assignments = new ConcurrentHashMap[K, V]().asScala

  //A specific key can be assigned only once, and subsequent calls to update with that key result in an exception.
  def update(k: K, v: V): Unit = {
    if (assignments.contains(k)) throw new IllegalArgumentException(s"Cannot put existing key into the map: $k")

    assignments.put(k, v)
    publisher.onNext(k)
  }

  //Calling apply with a specific key returns a future, which is completed after that key is inserted into the map.
  def apply(k: K): Future[V] = {
    val p = Promise[V]
    connectableObservable
      .filter(_ == k)
      .subscribe(
        //onNext
        key => p success assignments(key)
      )
    p future
  }
}

private trait IMapContract[K, V] {
  def update(k: K, v: V): Unit
  def apply(k: K): Future[V]
}