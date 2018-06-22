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
  def put(k: K, v: V):Option[(K, V)] = {
    //java's concurrent hash map doesn't allow null keys or values
    if(checkKey(k) && checkValue(v)){
      //if key is present and value is the same, nothing to do here
      if(containsKey(k) && Objects.equals(v, getValue(k).get)){
        return Some(k, v)
      }
      //should be safe to do get since our map doesn't contain null keys or values
      val response = getValue(k).map(oldValue=> (k, oldValue))
      byKeys.synchronized {byKeys.put(k, v)}
      byValues.synchronized {byValues.put(v, k)}
      response
    } else None
  }

  def removeFromBothMaps(k: K, v: V): Unit = {
    byKeys.synchronized{byKeys.remove(k)}
    byValues.synchronized{byValues.remove(v)}
  }

  def removeKey(k: K): Option[V] = this.synchronized {
    getValue(k).map(v => {
      removeFromBothMaps(k,v)
      v
    })
  }

  def removeValue(v: V):Option[K] = this.synchronized {
    getKey(v).map(k=> {
      removeFromBothMaps(k, v)
      k
    })
  }

  def size: Int = byKeys.size

  def iterator: Iterator[(K, V)] = byKeys.iterator

  def replace(k1: K, v1: V, k2: K, v2: V): Unit = {
    if(containsKey(k1) && Objects.equals(v1, getValue(k1).get)){
      removeKey(k1)
      put(k2, v2)
    }
  }
}