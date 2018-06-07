package org.learningconcurrency.concurrency_on_jvm

import scalabook.adt.SortedListPriorityQueue

object PriorityTaskPool extends App{

  import org.learningconcurrency._

  private val tasks = new SortedListPriorityQueue[Task]((t1, t2) => t1.compare(t2))

  case class Task(priority: Int, task: ()=> Unit) extends Ordered[Task]{
    override def compare(that: Task): Int = this.priority compare that.priority

    override def toString: String = s"task with priority $priority"
  }

  object Worker extends Thread {
    setDaemon(true)
    def poll() = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      val res = tasks.dequeue()
      res.task
    }

    override def run(): Unit = while(true) {
      val task = poll()
      task()
    }
  }

  Worker.start()
  def asynchronous(priority: Int)(task: () => Unit):Unit = tasks.synchronized {
    tasks.enqueue(Task(priority, task))
    tasks.notify()
  }

  asynchronous(2){ () => log("Hello ")}
  asynchronous(1){ () => log("Bonjour ")}
  asynchronous(2){ () => log("World!")}
  asynchronous(1){ () => log("le Monde!")}

  Thread.sleep(500)
}
