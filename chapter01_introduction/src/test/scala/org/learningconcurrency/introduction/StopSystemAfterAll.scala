package org.learningconcurrency.introduction

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

/*
  trait that makes sure that the system under test is automatically stopped
  when the unit test ends (from Akka in Action, chapter 2)
 */
trait StopSystemAfterAll extends BeforeAndAfterAll{
  /*
    Self-types are a way to declare that a trait must be mixed into another
    trait, event though it doesn't directly extend it. That makes the members
    of the dependency available without imports.
   */
  this: TestKit with Suite =>

  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }
}
