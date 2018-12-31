package org.learningconcurrency.futures_and_promises

import java.net.URL
import java.util.concurrent.TimeUnit

import scala.concurrent._
import scala.io._
import ExecutionContext.Implicits.global
import org.learningconcurrency.validators.Validable._

/**
  * Exercise 1 Implement a command-line program that asks the user to input
  * a URL of some website, and displays the HTML of that website. Between the
  * time that the user hits ENTER and the time that the HTML is retrieved, the
  * program should repetitively print a . to the standard output every 50 milliseconds
  * , with a 2 second timeout. Use only futures and promises, and avoid synchronization
  * primitives from the previous chapters. You may reuse the timeout method defined
  * in this chapter.
  */
object Ex01_FetchHtmlFromUrl extends App {

  val url: URL = validateType[URL](StdIn.readLine("Please enter the URL of the document you wish to see:\n"))

  timeout(2000) or printHtml(url)

  private def printHtml(url: URL): Future[Unit] = {
    val p = Promise[Unit]
    val future =
    Future {
        Source.fromURL(url).mkString
      }
    while (!future.isCompleted) {
      print(".")
      TimeUnit.MILLISECONDS.sleep(50)
    }
    println()
    future.foreach(print(_))
    p success()
    p.future
  }
}