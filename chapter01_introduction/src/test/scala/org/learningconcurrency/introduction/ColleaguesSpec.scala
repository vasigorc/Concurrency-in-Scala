package org.learningconcurrency.introduction

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class ColleaguesSpec extends TestKit(ActorSystem("colleaguessystem"))
  with AnyWordSpecLike with Matchers with StopSystemAfterAll {

  "A ColleaguesManager" must {
    "send an empty Iterable of its children in response to GetState" in {
      import org.learningconcurrency.introduction.ColleaguesManager._

      val colleaguesmanager = system.actorOf(Props[ColleaguesManager], "colleaguesmanager")
      Thread.sleep(10000)
      colleaguesmanager ! GetState(testActor)
      expectMsgPF(){
        case StateResponse(children) =>
          children.size must be(0)
      }
    }
  }
}
