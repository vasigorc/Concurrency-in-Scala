package org.learningconcurrency.introduction

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.learningconcurrency.introduction.Colleague.GameOver
import org.scalatest.{MustMatchers, WordSpecLike}
import scala.concurrent.duration._

class ColleaguesSpec extends TestKit(ActorSystem("colleaguessystem"))
  with WordSpecLike with MustMatchers with StopSystemAfterAll {

  "expect GameOver message for ColleaguesManager" in {
    val colleaguesmanager = system.actorOf(Props[ColleaguesManager], "colleaguesmanager")
    expectMsgType(10 seconds)[GameOver]
  }
}
