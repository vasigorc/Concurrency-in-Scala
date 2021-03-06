package org.learningconcurrency.concurrency_on_jvm

import java.lang.management.ManagementFactory

import org.learningconcurrency.{log, thread}

import scala.collection.mutable
import scala.reflect.ClassTag

object Exercises02 extends App{

  ex6()

  def parallel[A,B](a: =>A, b: =>B): (A,B) = { // ex 1
    var aholder = null.asInstanceOf[A]
    var bholder = null.asInstanceOf[B]

    val t1 = thread {
      aholder = a
      log(s"value $aholder calculated")
    }

    val t2 = thread {
      bholder = b
      log(s"value $bholder calculated")
    }

    t1.join(); t2.join()
    (aholder, bholder)
  }

  def periodically(duration: Long)(b: =>Unit):Unit ={ // ex 2
  //graceful shutdown
  object Worker extends Thread{
    var terminated = false

    def repeat():Option[() => Unit] = {
      Thread.sleep(duration)
      if(!terminated) Some(() =>b) else None
    }

    override def run(): Unit = repeat() match {
      case Some(action) => action(); run()
      case None =>
    }
  }
    Worker.start()
    Thread.sleep(duration*10)//after 10*times duration
    Worker.terminated = true
  }

  class SyncVar[T]{ // ex 3 - 5
    var t: T = null.asInstanceOf[T]

    def isEmpty: Boolean = this.synchronized(t == null)
    def nonEmpty: Boolean = this.synchronized(!isEmpty)

    def getWait:T = this.synchronized{
      while(isEmpty) this.wait()

      val temp = t
      t = null.asInstanceOf[T]
      this.notify()
      temp
    }

    def putWait(x: T):Unit = this.synchronized{
      while (nonEmpty) this.wait()

      t = x
      this.notify()
    }

    def get():T = this.synchronized{
      if(nonEmpty) {
        val temp = t
        t = null.asInstanceOf[T]
        temp
      } else throw new Exception("variable not initialized")
    }

    def put(x: T):Unit = this.synchronized{
      if(isEmpty){
        t = x
      } else {
        throw new Exception("variable not consumed yet")
      }
    }
  }

    def busyWaiting():Unit = {  // ex 4

    println(s"pid is ${ManagementFactory.getRuntimeMXBean.getName}")

    val syncVar = new SyncVar[Int]

    object Producer extends Thread{

      override def run(): Unit = {
        var i = 0
        while(i < 15){
          if(syncVar.isEmpty){
            syncVar.put(i)
            i+=1
          }
        }
      }
    }

  object Consumer extends Thread{

    override def run(): Unit = {
      var i = 0
      while (i < 15){
        if(syncVar.nonEmpty){
          log(s"get is ${syncVar.get()}")
          i+=1
        }
      }
    }
  }

    Producer.start()
    Consumer.start()

    Producer.join(); Consumer.join()
  }

  def ex5():Unit ={ // ex 5
    val syncVar = new SyncVar[Int]

    val producer= thread {
      var i = 0
      while(i < 15){
        syncVar.putWait(i)
        i+=1
    }
  }

    val consumer = thread {
      var i = 0
      while (i<15){
        log(s"get is ${syncVar.getWait}")
        i+=1
      }
    }

    producer.join(); consumer.join()
  }

  class SyncQueue[T](n: Int)(implicit classTag: ClassTag[T]){ // ex 6
    private val queue = new mutable.Queue[T]

    private def isEmpty = queue.isEmpty
    private def isFull = queue.size == n

    def getWait:T = queue.synchronized {
      while (isEmpty) queue.wait()

      val tmp = queue.dequeue()
      queue.notify()
      tmp
    }

    def putWait(x: T):Unit = queue.synchronized {
      while (isFull) queue.wait()

      queue.enqueue(x)
      queue.notify()
    }
  }

  def ex6():Unit ={ // ex 6
    val syncQueue = new SyncQueue[Int](15)

    val producer= thread {
      var i = 0
      while(i < 30){
        syncQueue.putWait(i)
        i+=1
      }
    }

    val consumer = thread {
      var i = 0
      while (i<15){
        log(s"get is ${syncQueue.getWait}")
        i+=1
      }
    }
    producer.join(); consumer.join()
  }
}
