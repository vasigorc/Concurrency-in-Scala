package org.learningconcurrency.concurrency_on_jvm

class PriorityTaskPool_Ex9 (nrWorkers: Int) extends PriorityTaskPool {

  assert(nrWorkers > 0 && nrWorkers < Int.MaxValue, "Invalid number of threads requested")

  // start eagerly waiting for new Tasks for all threads
  for(i <- 1 to nrWorkers){
    worker
  }

}