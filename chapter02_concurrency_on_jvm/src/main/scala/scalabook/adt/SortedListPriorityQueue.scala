package scalabook.adt

import java.util.PriorityQueue

import scala.reflect.ClassTag

//inspired from "Introduction to the Art of Programming using Scala" Ch. 26 Priority Queues
class SortedListPriorityQueue[A: ClassTag](compare: (A,A) => Int) extends PriorityQueue[A]{
  private class Node(var data: A, var prev: Node, var next: Node)
  private val end = new Node(null.asInstanceOf[A], null, null)
  end.next = end
  end.prev = end

  def enqueue(obj: A): Unit ={
    var rover = end.prev //rover -sth or smb who wanders
    while(rover!=end && compare(obj,rover.data) >0) rover = rover.prev
    rover.next.prev = new Node(obj,rover,rover.next)
    rover.next = rover.next.prev
  }

  def dequeue():A = {
    val ret = end.next.data
    end.next = end.next.next
    end.next.prev = end
    ret
  }

  override def peek:A = end.next.data

  override def isEmpty: Boolean = end.next == end

  def removeMatches(f: A => Boolean): Unit ={
    var rover = end.next //starting from the beginning this time
    while (rover != end){
      if(f(rover.data)){
        rover.prev.next = rover.next
        rover.next.prev = rover.prev
      }
      rover = rover.next
    }
  }
}
