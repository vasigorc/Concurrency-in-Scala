package org.learningconcurrency.concurrency_on_jvm

import java.util.Objects

import scala.collection.mutable.{HashMap => MyMap}

// for exercises 11 and 12
class ConcurrentBiMap [K, V] {
  private val byKeys = new MyMap[K, V]
  private val byValues = new MyMap[V, K]

  protected def checkKey(k: K): Boolean = k != null

  protected def checkValue(v: V): Boolean = v != null

  def containsKey(k: K): Boolean = byKeys.contains(k)

  def getKey(v: V):Option[K] = byValues.get(v)

  def getValue(k: K): Option[V] = byKeys.get(k)

  // should return previous value
  def put(k: K, v: V):Option[(K, V)] = this.synchronized {
    //java's concurrent hash map doesn't allow null keys or values
    if(checkKey(k) && checkValue(v)){
      //if key is present and value is the same, nothing to do here
      if(containsKey(k) && Objects.equals(v, getValue(k).get)){
        Some(k, v)
      }
      //should be safe to do get since our map doesn't contain null keys or values
      val oldKvPair: (K, V) = byKeys.get(k).map(oldValue=> (k, oldValue)).get
      byKeys.put(k, v)
      byValues.put(v, k)
      Some(oldKvPair)
    } else None
  }

  def removeKey(k: K): Option[V] = this.synchronized {
    byKeys.remove(k).map(value => {
      byValues.remove(value)
      value
    })
  }

  def removeValue(v: V):Option[K] = this.synchronized {
    byValues.remove(v).map(key => {
      byKeys.remove(key)
      key
    })
  }

  def size: Int = byKeys.size

  def iterator: Iterator[(K, V)] = byKeys.iterator
}
