package org.learningconcurrency.traditional_concurrency

import java.security.Permission
import org.learningconcurrency.traditional_concurrency._

//based on A. Prokopec's solution

/*
  This application receives the file path in which the serialized `Function0` object has ben written.
  Then, it reads and evaluates this serialized function object, and finally overrides its output to
  the same file.
 */
object Ex8_EvaluationApp extends App{

  /*
    SecurityManager - allows apps to implement a security policy. It allows to detect a possibly unsafe or sensitive
    operation. App can allow or disallow this operation.

    If a security manager is already installed and it doesn't allow self-replacement a SecurityException will be
    thrown.
   */
  System.setSecurityManager(new SecurityManager(){
    //allow access to the file
    override def checkPermission(perm: Permission): Unit = {}

    //detects `System.exit` in order not to be halted by the caller
    override def checkExit(status: Int): Unit = {
      throw new SecurityException(s"not allowed to pass a block which contains System.exit(int) !: $status")
    }

  })

  val path = args(0)

  new StreamGobbler(path).run()
}
