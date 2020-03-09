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
  val txt: String = ('a' until 'z').flatMap(c => List.fill(100)(() => c)).mkString
  val basicParString = new ParString(txt)
  val parStringWithParallelCombiner = new ParString(txt, new ParStringCombiner(ParStringCombiner.parResultImpl))

  private val parStrings: Gen[ParString] = Gen.enumeration("parStringImpls")(basicParString, parStringWithParallelCombiner)
  // tests
  performance of classOf[ParString].getSimpleName in {
    measure method "map" in {
      using(parStrings) in {
        impl => impl.map(_ toUpper)
      }
    }
  }
}