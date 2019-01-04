package org.learningconcurrency.futures_and_promises

import org.learningconcurrency.BaseSpec
import org.learningconcurrency.futures_and_promises.IVar.VALUE_NOT_SET

class IVarTest extends BaseSpec {

  private val className: String = classOf[IVar[_]].getSimpleName

  behavior of "apply"

  s"invoking apply on a new instance of $className" should "result in an exception" in {
    val ivar = new IVar[Int]
    an [Exception] should be thrownBy ivar.apply
    val valueNotSet = the [Exception] thrownBy ivar.apply
    valueNotSet.getMessage shouldEqual VALUE_NOT_SET
  }
}
