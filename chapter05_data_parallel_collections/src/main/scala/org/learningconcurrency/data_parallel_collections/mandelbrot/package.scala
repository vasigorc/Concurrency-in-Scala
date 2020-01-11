package org.learningconcurrency.data_parallel_collections

package mandelbrot {

  case class Complex(real: Double = 0.0, imaginary: Double = 0.0) {
    def +(that: Complex): Complex = op(that, _ + _)

    def -(that: Complex): Complex = op(that, _ - _)

    def *(that: Complex): Complex = op(that, _ * _)

    private def op(that: Complex, f: (Double, Double) => Double) = Complex(f(real, that.real), f(imaginary, that.imaginary))
  }

}