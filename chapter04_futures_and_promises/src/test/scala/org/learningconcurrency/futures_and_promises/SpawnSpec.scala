package org.learningconcurrency.futures_and_promises

import org.scalatest.funspec.AsyncFunSpec

class SpawnSpec extends AsyncFunSpec{

  describe("spawn") {
    it("will return exit code 0 when listing files/directories in $HOME") {
      val homeDir = System.getenv("HOME")
      spawn(s"ls -l $homeDir") map(result => assert(result == 0))
    }
  }
}
