package org.learningconcurrency.validators

import java.net.URL

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

trait Validable[T] {
  def validate(input: String): T
}

object Validable {
  implicit val boolean: Validable[Boolean] = new Validable[Boolean] {
    def validate(input: String): Boolean = {
      val simplifiedInput = standardizeInput(input)

      if(simplifiedInput.matches("[yn]")){
        simplifiedInput match {
          case "y" => true
          case "n" => false
        }
      }else{
        validate(StdIn.readLine(s"$simplifiedInput is not a valid "
          +"answer, please try again\n"))
      }
    }
  }


  implicit val int: Validable[Int] = new Validable[Int]{
    def validate(input: String): Int = {
      val simplifiedInput = standardizeInput(input)

      val attempt = Try(simplifiedInput toInt)
      attempt match {
        case Success(response) => response
        case Failure(_) => validate(StdIn.readLine(s"$simplifiedInput is not a valid "
          +"answer, please try again\n"))
      }
    }
  }

  implicit val url: Validable[URL] = new Validable[URL] {
    override def validate(input: String): URL = {
      val simplifiedInput = standardizeInput(input)

      val triedUrl = Try(new URL(simplifiedInput))
      triedUrl match {
        case Success(value) => value
        case Failure(_) => validate(StdIn.readLine(s"$simplifiedInput is not a valid " +
          s"url, please try again\n"))
      }
    }
  }

  def validateType[T](input: String)(implicit validable: Validable[T]): T =
    validable.validate(input)

  private def standardizeInput(input: String) = {
    input.replaceAll("[ \\n]", "").toLowerCase()
  }
}
