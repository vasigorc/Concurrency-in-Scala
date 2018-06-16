package vkostyukov

/**
  * copied from: http://vkostyukov.net/posts/combinatorial-algorithms-in-scala/
  * This tiny class might be used as follows
  * import CombinatorialOps._
  * val c = List(1, 2, 3).xcombinations(2)
  * Optimistic Programming: the body of recursive function may be implemented in terms of following ideas:
  * (a) when called recursively it gives the right answer for any sub-problem
  * but (b) some additional work should be done in order to merge these sub-problem solutions into the single solution of the entire problem.
  * The recursive function is pretended to be correctly implemented before its body is actually being written.
  */
object CombinatorialOps {
  implicit class CombinatorialList[A](l: List[A]) {

    /*
      An optimistic programming guarantees that a recursive function being called on a sub-problem produces a correct answer.
      A sub-problem of generating k-combinations is generating (k-1)-combinations.
     */
    def xcombinations(n: Int): List[List[A]] =
      if (n > l.size) Nil
      else l match {
        case _ :: _ if n == 1 =>
          l.map(List(_))
        case head :: tail =>
          tail.xcombinations(n - 1).map(head :: _) ::: tail.xcombinations(n)
        case _ => Nil
      }

    /**
     * The implementation is straightforward - combinations of all the possible sizes should be merged together.
     * That may be done by List’s foldLeft operation. The total number of subsets might be obtained from variations formula:
     *
     *                  S_n = sum(i=1..n) {C_i,n} = 2^n
     *
     * Time - O(S_n)
     * Space - O(S_n) There are 2^n subset of an n-size set. It’s choice of two: every set’s element is either taken or not into the particular subset.
     */
    def xsubsets: List[List[A]] = (2 to l.size).foldLeft(l.xcombinations(1))((a,i) => l.xcombinations(i) ::: a)

    def xvariations(n: Int): List[List[A]] = ???
    def xpermutations: List[List[A]] = ???

  }
}