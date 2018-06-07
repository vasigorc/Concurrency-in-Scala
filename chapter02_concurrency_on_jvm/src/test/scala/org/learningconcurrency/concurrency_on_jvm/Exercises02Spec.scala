package org.learningconcurrency.concurrency_on_jvm

import org.learningconcurrency.concurrency_on_jvm.ex02_07._
import org.learningconcurrency.{BaseSpec, thread}

class Exercises02Spec extends BaseSpec{

  import org.learningconcurrency.concurrency_on_jvm.Exercises02._

  "parallel" should "return a tuple with the result values of passed by-name parameters" in{
    val bynameA = math.random()
    val bynameB = math.random()
    parallel(bynameA, bynameB) should equal((bynameA, bynameB))
  }

  "sequential calls to a SyncVar's put() and get()" should "return initial value" in {
    val sync = new SyncVar[Int]

    val t1 = thread {
      sync.put(10)
    }

    t1.join()
    sync.get() should equal(10)
  }

  "transferring all money from acounts to target" should "result 0 money in all three and all money in target" in {
    val accounts: Set[Account] = Seq(new Account("Jack", 1000), new Account("Jill", 2000), new Account("Tom", 2000)).toSet
    val target = new Account("Mark", 0)
    sendAll(accounts, target)
    (0 /: accounts)((sum, account) => sum+account.money) shouldEqual 0
    target.money shouldEqual 5000
  }
}
