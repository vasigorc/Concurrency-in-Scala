package org.learningconcurrency.introduction

import akka.actor.ActorSystem

object ColleaguesApp extends App {

  import ColleaguesManager._

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val manager = system.actorOf(props, "colleagues_manager")
}
