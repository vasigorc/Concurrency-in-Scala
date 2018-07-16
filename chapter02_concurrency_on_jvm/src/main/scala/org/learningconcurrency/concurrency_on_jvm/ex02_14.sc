import scala.collection.mutable

/*
  Chapter 2 Ex 14
  Implement a cache method, which converts any function into a memoized version
  of itself. The first time that the resulting function is called for any
  argument it is called in the same way as the original function. However, the result is memoized
  and subsequentlyinvoking the resulting function with the same arguments must return
  the previously returned value
  NB: solution taken from: https://stackoverflow.com/questions/16257378/is-there-a-generic-way-to-memoize-in-scala
 */
def cache[K, V](f: K => V):K =>V = new mutable.HashMap[K, V] {
  self =>
  override def apply(key: K): V = self.synchronized(getOrElseUpdate(key, f(key)))
}

val tolerance = 0.0001

def abs(x: Double) = if (x >= 0) x else -x

def isCloseEnough(x: Double, y: Double)=
  abs((x-y)/x)/x<tolerance

def fixedPoint(f: Double => Double)(firstGuess: Double)={
  def iterate(guess: Double):Double ={
    val next = f(guess)
    if(isCloseEnough(guess, next)) next
    else iterate(next)
  }
  iterate(firstGuess)
}

//averaging successive values
def averageDump(f: Double => Double)(x: Double) = (x + f(x))/2

def sqrt(x: Double) = fixedPoint(averageDump(y => x/y))(1.0)

val memoizedSqrt = cache(sqrt _)
memoizedSqrt(4)