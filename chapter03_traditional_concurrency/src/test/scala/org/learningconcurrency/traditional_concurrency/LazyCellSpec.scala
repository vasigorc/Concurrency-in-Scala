package org.learningconcurrency.traditional_concurrency

import org.learningconcurrency.BaseSpec

class LazyCellSpec extends BaseSpec{

  "Calling LazyCell with a by name parameter" should "evaluate parameter exactly once" in {
    var counter = 0
    val lazyCell = new LazyCell({
      info("I am evaluated")
      counter+=1
      42
    })
    lazyCell()
    lazyCell()
    lazyCell()
    counter shouldEqual 1
  }
}
