package org.learningconcurrency.data_parallel_collections.binaryheapsplitter

import org.learningconcurrency.data_parallel_collections.binaryheapsplitter.BinaryHeapSplitter.HeapOps
import org.scalameter
import org.scalameter.Measurer
import org.scalameter.api._
import org.scalameter.picklers.Implicits._
import org.scalameter.reporting.RegressionReporter.Historian.ExponentialBackoff
import org.scalameter.reporting.RegressionReporter.Tester.Accepter

import scala.reflect.ClassTag
import scala.util.Random

object ParHeapBenchMark extends org.scalameter.Bench[Double] with Serializable {

  override def executor: Executor[Double] = LocalExecutor(
    new scalameter.Executor.Warmer.Default,
    Aggregator.min[Double],
    measurer
  )

  override def measurer: Measurer[Double] = new Measurer.IgnoringGC

  override def reporter: Reporter[Double] =
    new RegressionReporter[Double](Accepter(), ExponentialBackoff())

  override def persistor: Persistor = Persistor.None

  private val random = new Random()

  val sizes: Gen[Int] = Gen.enumeration("Input size")(10, 30, 50, 100)

  final case class InputState[T: ClassTag](
      value: Array[T],
      private val generateT: () => T
  ) {

    import InputState._

    def add(next: Int): InputState[T] =
      new InputState(value ++ generateArrayOfT(next, generateT), generateT)
  }

  object InputState {
    def generateArrayOfT[T: ClassTag](size: Int, generateT: () => T): Array[T] =
      Array.fill(size)(generateT())
    def empty[T: ClassTag](generateT: () => T): InputState[T] =
      new InputState[T](Array[T](), generateT)
  }

  performance of "foreach" in {
    performance of s"${Heap.getClass.getName}" in {
      val testHeap: Heap[Int] =
        Heap.fromArray(InputState.generateArrayOfT(100, () => random.nextInt()))
      var i      = 0
      val stream = testHeap.toStream
      stream.foreach { (entry: Int) =>
        i += entry
      }
//      using(sizes) in { inputSize =>
//        var i = 0
//        inputState = inputState.add(inputSize)
//        val stream = testHeap.toStream
//        stream.foreach{
//          (entry: Int) => i += entry
//        }
//      }
    }
  }

  performance of "foreach" in {
    performance of s"${ParHeap.getClass.getName}" in {
      val testHeap: ParHeap[Int] =
        Heap
          .fromArray(InputState.generateArrayOfT(100, () => random.nextInt()))
          .par
      var i = 0
      testHeap.foreach { (entry: Int) =>
        i += entry
      }
//      using(sizes) in { inputSize =>
//        var i = 0
//        inputState = inputState.add(inputSize)
//        Heap.fromArray(inputState.value).par.foreach((entry: Int) => i += entry)
//      }
    }
  }
}
