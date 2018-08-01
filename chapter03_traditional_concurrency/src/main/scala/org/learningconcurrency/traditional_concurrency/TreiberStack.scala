package org.learningconcurrency.traditional_concurrency

import java.util.concurrent.atomic.AtomicReference

/**
  Ex 2
  Implement a TreiberStack class, which implements a concurrent stack
  abstraction.
  Use an atomic variable that points to a linked list of nodes that were previously
  pushed to the stack. Make sure that your implementation is lock-free and not susceptible to
  the ABA problem.

  Implementation is based on Ex_1.3.50 from Sedgewick and Wayne's 'Algorithms':
  @see <a href="https://github.com/vasigorc/Ex_1.3.50/blob/master/src/main/java/ca/vgorcinschi/ex1_3_50/Stack.java">Ex_1.3.50</a>
 */
class TreiberStack [Item]{

  private val first = new AtomicReference[Node]()

  def push(x: Item): Unit = {
    val ov: Node = first.get()
    val valueChanged: Boolean = first.compareAndSet(ov, Node(x, ov))
    if (!valueChanged) push(x)
  }

  def pop():Item = {
    val ov: Node = first.get()
    if(ov == null) throw new NoSuchElementException
    val valueChanged: Boolean = first.compareAndSet(ov, ov.next)
    if(valueChanged) ov.item
    else pop()
  }

  case class Node(item: Item, next: Node)
}