package org.learningconcurrency.traditional_concurrency

/*
  Exercise 3. Implement a ConcurrentSortedList class, which implements a concurrent sorted list abstraction

  Under the hood, the ConcurrentSortedList class should use a linked list of atomic references. Ensure that your
  implementation is lock-free and avoids ABA problems.

  The Iterator object returned by the iterator method must correctly traverse the elements of the list in ascending
  order under the assumption that there are no concurrent invocations of the add method.
 */
import java.util.concurrent.atomic.AtomicReference

class ConcurrentSortedList[T] (implicit val ord:Ordering[T]){

  case class Node(item: T, next: AtomicReference[Option[Node]] = new AtomicReference[Option[Node]](None))

  val first = new AtomicReference[Option[Node]](None)

  private def add(node: AtomicReference[Option[Node]], x: T): Unit = {
    import Ordered._

    val head = node.get
    head match {
      case None => if(!first.compareAndSet(None, Some(Node(x))))
        add(node, x)
      case cur @ Some(Node(y, next)) => if(x <= y){
        if(!first.compareAndSet(cur, Some(Node(x, new AtomicReference[Option[Node]](cur)))))
          add(node, x)
      } else {
        add(next, x)
      }
    }
  }

  def add(x: T): Unit ={
    add(first, x)
  }

  def iterator: Iterator[T] = new Iterator[T] {
    private var current = first.get

    override def hasNext: Boolean = current.nonEmpty

    override def next(): T = {
      val answer = current.get
      current = answer.next.get()
      answer.item
    }
  }
}
