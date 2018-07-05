package org.learningconcurrency.introduction

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestKit
import org.learningconcurrency.introduction.ColleaguesManager.GetState
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.collection.immutable
import scala.concurrent.duration._

class ColleaguesSpec extends TestKit(ActorSystem("colleaguessystem"))
with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "expect GameOver message for ColleaguesManager" in {
    val colleaguesmanager = system.actorOf(Props[ColleaguesManager], "colleaguesmanager")
    colleaguesmanager ! GetState(testActor)
    expectMsg(30 seconds, immutable.Iterable[ActorRef]())
  }
}
