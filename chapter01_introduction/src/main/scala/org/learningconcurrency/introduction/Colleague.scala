package org.learningconcurrency.introduction

import java.time.LocalTime
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.learningconcurrency.randomTime

import scala.collection._
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.util.Random

class Colleague(friends: Seq[String]) extends Actor with ActorLogging{
  import org.learningconcurrency.introduction.Colleague._

  var time: Option[LocalTime] = None
  val agreedParties: mutable.Set[ActorRef] = mutable.Set[ActorRef]()

  inviteSomeone()

  private def inviteSomeone(): Unit = {
    val delay = Random.nextInt(10000)
    if(time.isEmpty){
      //messaging is only initialized if no meetup message is already received
      agreedParties += self
      implicit val ec: ExecutionContextExecutor = context.dispatcher
      context.system.scheduler.scheduleOnce(delay milliseconds){
        inviteUnreachedColleague()
      }
    }

  }

  override def receive: Receive = {
    case MeetUp(suggestedTime, attenders) =>
      if(time.isEmpty) {
        acceptInvite(suggestedTime, attenders)
        sender() ! ConfirmMeetUp(suggestedTime, agreedParties)
      } else if(attenders.size > agreedParties.size){
        acceptInvite(suggestedTime, attenders)
      } else {
        time.foreach(sender() ! MeetUp(_, agreedParties))
      }
    case ConfirmMeetUp(suggestedTime, attenders) =>
      acceptInvite(suggestedTime, attenders)
  }

  def acceptInvite(suggestedTime: LocalTime, attenders: Set[ActorRef]):Unit = {
    time = Some(suggestedTime)
    agreedParties += self
    agreedParties ++: attenders
    if(agreedParties.size >= 3) context.parent ! GameOver(time.get, agreedParties)
    else inviteUnreachedColleague()

  }

  private def inviteUnreachedColleague(): Unit = {
    randomUnreached.foreach(unreached => {
      log.info("colleague {} sends message to a random friend({})", self.path.name, unreached)
      context.actorSelection(s"../$unreached") ! MeetUp(randomTime, agreedParties)
    })
  }

  def randomUnreached: Option[String] = {
    val yetUnreached = friends.diff(agreedParties.map(_.path.name).toSeq)
    if(yetUnreached.isEmpty)None
    else Some(yetUnreached(Random.nextInt(yetUnreached.size)))
  }
}

object Colleague {

  sealed trait Msg
  case class MeetUp(time: LocalTime, attenders: Set[ActorRef]) extends Msg
  case class ConfirmMeetUp(time: LocalTime, attenders: Set[ActorRef]) extends Msg
  case class GameOver(time: LocalTime, attenders: Set[ActorRef]) extends Msg

  val name = s"colleague_${UUID.randomUUID().toString}"
  val path = s"user/$name"
  def props(friends: Seq[String]) = Props(new Colleague(friends))
}
