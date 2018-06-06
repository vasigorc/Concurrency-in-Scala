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

    t1.join(); t2.join();
    (aholder, bholder)
  }
}
