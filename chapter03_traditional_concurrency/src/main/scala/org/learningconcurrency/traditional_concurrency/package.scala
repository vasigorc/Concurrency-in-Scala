package org.learningconcurrency

import java.io._
import java.util.concurrent.Callable
import java.util.regex.Pattern

import scala.util.Try

package object traditional_concurrency {

  import scala.sys.process._
  import org.learningconcurrency._
  /*
    Exercise 8. Implement a method spawn that given a block of Scala code, starts a new
    JVM process and runs the specified block in the new process.

    Once the block return a value, the spawn method should return the value from the child process.
    If the block throws an exception, the spawn should throw the same exception.

    Use Java serialization to transfer the block code, its return value and the potential
    exceptions between the parent and the child JVM processes.
   */
  def spawn[T](block: => T): T = {
    val className = Ex8_EvaluationApp.getClass.getName.split(Pattern.quote("$"))(0)
    val tmp = File.createTempFile("concurrent-programming-in-scala", null)
    tmp.deleteOnExit()

    val out = new ObjectOutputStream(new FileOutputStream(tmp))
    withRessources(out){_ =>
      out.writeObject(() => block)
    }

    val ret = s"java -cp ${System.getProperty("java.class.path")} $className ${tmp.getCanonicalPath}".!

    if (ret != 0)
      throw new RuntimeException("fails to evaluate block in a new JVM process")

    val in = new ObjectInputStream(new FileInputStream(tmp)) //write to the same file
    withRessources(in){_ =>
      in readObject() match {
        case e: Throwable => throw e
        case x => x.asInstanceOf[T]
      }
    }
  }

  class StreamGobbler(filePath: String) extends Runnable{
    override def run(): Unit = {
    val in = new ObjectInputStream(new FileInputStream(filePath))
      withRessources(in){ _ =>
        val f0 = in.readObject().asInstanceOf[()=>Any]
        val out = new ObjectOutputStream(new FileOutputStream(filePath))
        withRessources(out){_ =>
          Try(out.writeObject(f0())) recoverWith { case t => Try(out.writeObject(t)) }
        }
      }
    }
  }
}
