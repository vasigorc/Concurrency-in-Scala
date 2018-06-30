package org.learningconcurrency.introduction

import akka.actor.{Actor, ActorLogging, SupervisorStrategy}

class ColleaguesManager extends Actor with ActorLogging{

  override def preStart(): Unit = ???

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  override def receive: Receive = ???
}
