package org.learningconcurrency.concurrency_on_jvm

// ex 8
object PriorityTaskPool_Ex8 extends App with PriorityTaskPool {

  import org.learningconcurrency._

  worker

  asynchronous(2){ () => log("Hello ")}
  asynchronous(1){ () => log("Bonjour ")}
  asynchronous(2){ () => log("World!")}
  asynchronous(1){ () => log("le Monde!")}

  Thread.sleep(500)
}
