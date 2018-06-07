package org.learningconcurrency.concurrency_on_jvm

import java.util.concurrent.{ForkJoinPool, RecursiveAction}
import org.learningconcurrency._

object ex02_07 extends App{

  object SynchronizedProtectedUid{
    private var uidCount = 0L

    def getUniqueId() = uidCount.synchronized {
      val freshUid = uidCount +1
      uidCount = freshUid
      freshUid
    }
  }

  class Account(val name: String, var money: Int){
    val uid = SynchronizedProtectedUid.getUniqueId()

    override def toString: String = name.concat(", uid: "+uid)
  }

  //p. 50
  def send(a1: Account, a2: Account, n: Int): Unit ={
    def adjust(): Unit ={
      log(s"transferring ${a1.money} from $a1 to $a2")
      a1.money -= n
      a2.money += n
    }

    //lock objects universally in same order
    if(a1.uid < a2.uid)
      a1.synchronized {a2.synchronized {adjust()}}
    else a2.synchronized { a1.synchronized {adjust()}}
  }

  case class TransferTask(accounts: Set[Account], target: Account) extends RecursiveAction {

    import java.util.concurrent.ForkJoinTask._

    override def compute(): Unit = accounts.size match {
      case 0 =>
      case 1 => {
        val head = accounts.head
        send(head, target, head.money)
      }
      case _ => invokeAll(TransferTask(accounts.drop(1), target), TransferTask(Set(accounts.head), target))
    }
  }

  /**
    * method transfers all the money from every account
    * in accounts to the target
    * @param accounts - set of bank accounts
    * @param target - bank account
    */
  def sendAll(accounts: Set[Account], target: Account):Unit = {
    val transferTask = TransferTask(accounts, target)
    val forkJoinPool = new ForkJoinPool()
    forkJoinPool.invoke(transferTask)
  }
}
