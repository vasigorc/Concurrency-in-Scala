package org.learningconcurrency.concurrency_on_jvm

import java.util.concurrent.atomic.AtomicBoolean

class PriorityTaskPool_Ex10(p: Int, important: Int) extends PriorityTaskPool_Ex9(p) {

  private var poolClosed: AtomicBoolean = AtomicBoolean

  private class AttentiveWorker extends Worker {

    override def poll(): Option[Task] = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      if(!poolClosed.get() || tasks.peek.priority > important) Some(tasks.dequeue()) else None
    }

    override def run(): Unit = poll() match {
      case Some(task) => task(); run()
      case None =>
    }
  }

  def shutdown(): Unit = {
    poolClosed.getAndSet(true)
  }
}
