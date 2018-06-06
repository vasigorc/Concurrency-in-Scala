package org.learningconcurrency

object Exercises02 {

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
      case Some(action) => action; run()
      case None =>
    }
  }
    Worker.start()
    Thread.sleep(duration*10)//after 10*times duration
    Worker.terminated = true
  }

  class SyncVar[T]{ // ex 3
    var t: T = null.asInstanceOf[T]

    def get():T = this.synchronized{
      if(t != null) {
        val temp = t
        t = null.asInstanceOf[T]
        temp
      } else throw new Exception("variable not initialized")
    }

    def put(x: T):Unit = this.synchronized{
      if(t == null){
        t = x
      } else {
        throw new Exception("variable not consumed yet")
      }
    }
  }
}
