package org.learningconcurrency.data_parallel_collections.parstring

import org.scalameter
import org.scalameter.{Executor, Persistor, Reporter, api}
import org.scalameter.api._
import org.scalameter.picklers.Implicits._
import org.scalameter.picklers.noPickler._

import scala.language.postfixOps

object ParStringBenchMark extends org.scalameter.Bench[Double] with Serializable {

  // configuration
  def executor: Executor[Double] = LocalExecutor(
    new scalameter.Executor.Warmer.Default,
    Aggregator.min[Double],
    measurer
  )

  def measurer: Measurer[Double] = new api.Measurer.IgnoringGC

  def reporter: Reporter[Double] = new LoggingReporter[Double]

  def persistor: Persistor = Persistor.None

  // benchmarks
  val txt: String = "A custom text" * 25000
  val basicParString = new ParString(txt)
  val parStringWithParallelCombiner = new ParString(txt, new ParStringCombiner(ParStringCombiner.parResultImpl))

  // tests
  performance of classOf[ParString].getSimpleName in {
    measure method "map" in {
      using(Gen.single("basicParString")(basicParString)) in {
        impl => impl.map(_ toUpper)
      }

      using(Gen.single("ex6")(parStringWithParallelCombiner)) in {
        impl => impl.map(_ toUpper)
      }
    }
  }
}