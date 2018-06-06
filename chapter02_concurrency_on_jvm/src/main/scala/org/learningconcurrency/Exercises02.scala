package org.learningconcurrency

import java.lang.management.ManagementFactory

object Exercises02 extends App{

  busyWaiting()

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

  class SyncVar[T]{ // ex 3 - 6
    var t: T = null.asInstanceOf[T]

    def isEmpty = this.synchronized(t == null)
    def nonEmpty = this.synchronized(!isEmpty)

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
    val syncVar = new SyncVar[Int]

    println(s"pid is ${ManagementFactory.getRuntimeMXBean.getName}")

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
}
