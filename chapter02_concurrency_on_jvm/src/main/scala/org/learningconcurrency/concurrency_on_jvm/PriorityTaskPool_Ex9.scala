package org.learningconcurrency.concurrency_on_jvm

class PriorityTaskPool_Ex9 (nrWorkers: Int) extends PriorityTaskPool {

  assert(nrWorkers > 0 && nrWorkers < Int.MaxValue, "Invalid number of threads requested")

  // start eagerly waiting for new Tasks for all threads
  (1 to nrWorkers).map((i) => new Worker()).map(_.start)
}

object PriorityTaskPool_Ex9 extends App{

  import org.learningconcurrency._

  def apply(nrWorkers: Int): PriorityTaskPool_Ex9 = new PriorityTaskPool_Ex9(nrWorkers)

  val brothersKaramazov = "The world says: \"You have needs -- satisfy them. You have as much right as the rich and " +
    "the mighty. Don't hesitate to satisfy your needs; indeed, expand your needs and demand more.\" This is the worldly " +
    "doctrine of today. And they believe that this is freedom. The result for the rich is isolation and suicide, for " +
    "the poor, envy and murder."

  private val words: List[(String, Int)] = brothersKaramazov.split(" ").zipWithIndex.toList

  //creating a PriorityTaskPool with 4 worker threads
  private val pool_Ex = PriorityTaskPool_Ex9(4)

  for ((word, order) <- words){
    pool_Ex.asynchronous(words.size - order){() => log(word)}
  }

  Thread.sleep(1000)
}