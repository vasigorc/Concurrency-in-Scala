package org.learningconcurrency.concurrency_on_jvm

import scalabook.adt.SortedListPriorityQueue

trait PriorityTaskPool {

  protected val tasks = new SortedListPriorityQueue[Task]((t1, t2) => t1.compare(t2))

  case class Task(priority: Int, task: ()=> Unit) extends Ordered[Task]{
    override def compare(that: Task): Int = this.priority compare that.priority

    override def toString: String = s"task with priority $priority"
  }

  protected class Worker extends Thread {
    setDaemon(true)
    def poll() = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      tasks.dequeue()
    }

    override def run() = {
      while (true) {
        poll() match {
          case Task(_, task) => task()
        }
      }
    }
  }

  def asynchronous(priority: Int)(task: () => Unit):Unit = tasks.synchronized {
    tasks.enqueue(Task(priority, task))
    tasks.notifyAll()
  }
}
