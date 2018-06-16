package org.learningconcurrency.concurrency_on_jvm

import java.util.concurrent.atomic.AtomicBoolean

class PriorityTaskPool_Ex10(p: Int, important: Int) extends PriorityTaskPool {

  private var poolClosed: AtomicBoolean = new AtomicBoolean(false)

  (1 to p).map((i) => new AttentiveWorker()).map(_.start)

  private class AttentiveWorker extends Worker {

    def pollOpt(): Option[Task] = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      if(!poolClosed.get() || tasks.peek.priority > important) Some(tasks.dequeue()) else None
    }

    override def run(): Unit = pollOpt() match {
      case Some(task) => task.task(); run()
      case None =>
    }
  }

  def shutdown(): Unit = {
    poolClosed.getAndSet(true)
  }
}

object PriorityTaskPool_Ex10 extends App{

  import org.learningconcurrency._

  def apply(p: Int, important: Int): PriorityTaskPool_Ex10 = new PriorityTaskPool_Ex10(p, important)

  private val pool_Ex = PriorityTaskPool_Ex10(4, 30)

  (1 to 60).foreach((i) => {
    val a = (Math.random*1000).toInt
    pool_Ex.asynchronous(60-a){()=>log(s"<- $a")}
  })
  pool_Ex.shutdown()
  Thread.sleep(5000)
}