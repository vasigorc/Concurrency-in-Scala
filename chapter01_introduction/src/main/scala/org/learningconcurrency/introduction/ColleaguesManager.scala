package org.learningconcurrency.introduction

import akka.actor.{Actor, ActorLogging, PoisonPill, SupervisorStrategy}

import scala.collection.immutable

class ColleaguesManager extends Actor with ActorLogging{
  import Colleague._

  override def preStart(): Unit = {
    val colleagues: immutable.Seq[String] = (1 to 4).map("colleague_"+_)
    colleagues.foreach(c => props(colleagues.diff(Seq(c))))
  }

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  override def receive: Receive = {
    case GameOver(time, attenders) =>
      log.info("{} agreed to meet at {} in a local bar", attenders.mkString(", "), time)
      context.children.foreach(_ ! PoisonPill)
      context.system.terminate()
  }
}
