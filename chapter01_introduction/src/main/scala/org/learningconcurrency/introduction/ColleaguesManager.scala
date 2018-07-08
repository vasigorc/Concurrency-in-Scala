package org.learningconcurrency.introduction

import akka.actor._
import org.learningconcurrency.introduction.ColleaguesManager.{GetState, StateResponse}

import scala.collection.immutable

class ColleaguesManager extends Actor with ActorLogging{
  import Colleague._

  override def preStart(): Unit = {
    super.preStart()
    val colleagues: immutable.Seq[String] = (1 to 4).map("colleague_"+_)
    colleagues.foreach(aName => context.actorOf(props(colleagues.diff(Seq(aName))), aName))
  }

  override def receive: Receive = {
    case GameOver(time, attenders) =>
      log.info("{} agreed to meet at {} in a local bar", attenders.mkString(", "), time)
      context.children.foreach(_ ! PoisonPill)
    case GetState(receiver) => receiver ! StateResponse(context.children)
  }
}

object ColleaguesManager{
  val name = s"colleagues manager"
  val path = s"user/$name"

  case class GetState(receiver: ActorRef)
  case class StateResponse(children: Iterable[ActorRef])

  def props()= Props(new ColleaguesManager())
}
