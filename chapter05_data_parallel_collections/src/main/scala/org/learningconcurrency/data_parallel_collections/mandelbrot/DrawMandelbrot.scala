package org.learningconcurrency.data_parallel_collections.mandelbrot

import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color

import scala.collection.parallel.CollectionConverters.ImmutableSeqIsParallelizable

/**
 * Exercise 03. Implement parallel rendering of the Mandelbrot Set
 */
object DrawMandelbrotSet extends JFXApp {

  val (rMin, rMax) = (-1.5, 0.5)
  val (iMin, iMax) = (-1.0, 1.0)
  val w = 600
  val h = 600
  val maxCount: Int = 100

  /**
   * Basic example of writing pixels to images
   * with JavaFX is taken from: https://docs.oracle.com/javafx/2/image_ops/jfxpub-image_ops.htm
   */
  stage = new PrimaryStage {
    title = "Mandelbrot Set"
    val mandelbrotImage = draw()
    val imageView = new ImageView(mandelbrotImage)
    val stackPane: StackPane = new StackPane()
    stackPane.getChildren.add(imageView)
    scene = new Scene(w, h) {
      root = stackPane
    }
  }


  def mandelCount(complex: Complex): Int = {
    def mandelIter(z: Complex): Complex = {
      val squareOfZ = z * z
      // f(c) = z ^ 2 + c -> https://en.wikipedia.org/wiki/Mandelbrot_set#Formal_definition
      Complex(squareOfZ.real - squareOfZ.imaginary + complex.real, 2 * (z.real * z.imaginary) +  complex.imaginary)
    }

    def mandelCountHelper(iterationCount: Int, z: Complex): Int = {
      // Pythagorean Theorem proof: https://en.wikipedia.org/wiki/Mandelbrot_set#Escape_time_algorithm
      val squareOfZ = z * z
      if (iterationCount >= maxCount || squareOfZ.real + squareOfZ.imaginary >= 4) {
        return iterationCount
      }
      mandelCountHelper(iterationCount + 1, mandelIter(z))
    }
    mandelCountHelper(0, Complex())
  }

  /**
   * algorithm implementation is largely based on an example from
   * the book "Object Orientation, Abstraction and Data Structures Using Scala"
   * by Mark C. Lewis
   * @return image of Mandelbrot set
   */
  private def draw(): Image = {
    def isInSet: Int => Boolean = _ == maxCount

    val image = new WritableImage(w, h)
    val writer = image.pixelWriter
    // parallelizeable part
    for (i <- (0 until w).par) {
      val real = rMin + i * (rMax - rMin) / w
      for (j <- 0 until h) {
        val imaginary = iMax - j * (iMax - iMin) / h
        val count = mandelCount(Complex(real, imaginary))
        val color: Color = if (isInSet(count)) {
          Color.Black
        } else {
          Color(1.0, 0.0, 0.0, math.log(count.toDouble) / math.log(maxCount))
        }
        writer.setColor(i, j, color)
      }
    }
    image
  }
}