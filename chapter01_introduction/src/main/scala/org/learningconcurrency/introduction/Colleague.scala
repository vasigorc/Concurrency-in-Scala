package org.learningconcurrency.introduction

import java.time.LocalTime
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.learningconcurrency.randomTime

import scala.collection._
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.util.Random

class Colleague(friends: Seq[ActorRef]) extends Actor with ActorLogging{
  import org.learningconcurrency.introduction.Colleague._

  var time: Option[LocalTime] = None
  val agreedParties: mutable.Set[ActorRef] = mutable.Set[ActorRef](self)

  inviteSomeone()

  private def inviteSomeone(): Unit = {
    val delay = Random.nextInt(10000)
    if(time.isEmpty){
      //messaging is only initialized if no meetup message is already received
      implicit val ec: ExecutionContextExecutor = context.dispatcher
      context.system.scheduler.scheduleOnce(delay milliseconds){
        randomUnreached.foreach(unreached => {
          log.info("colleague {} sends message to a random friend", unreached.path.name)
          unreached ! MeetUp(randomTime, agreedParties)
        })
      }
    }

  }

  override def receive: Receive = {
    case MeetUp(suggestedTime, attenders) => {
      if(time.isEmpty) {
        time = Some(suggestedTime)
        agreedParties ++: attenders
        sender() ! ConfirmMeetUp(suggestedTime, agreedParties)
      }
    }
  }

  def randomUnreached: Option[ActorRef] = {
    val yetUnreached = friends.diff(agreedParties.toSeq)
    if(yetUnreached.isEmpty)None
    else Some(yetUnreached(Random.nextInt(yetUnreached.size)))
  }
}

object Colleague {

  case class MeetUp(time: LocalTime, attenders: Set[ActorRef])
  case class ConfirmMeetUp(time: LocalTime, attenders: Set[ActorRef])

  val name = s"colleague_${UUID.randomUUID().toString}"
  val path = s"user/$name"
  def props(friends: Seq[ActorRef]) = Props(new Colleague(friends))
}
