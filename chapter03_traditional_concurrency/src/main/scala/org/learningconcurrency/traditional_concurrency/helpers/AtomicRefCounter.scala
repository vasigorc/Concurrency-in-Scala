package org.learningconcurrency.traditional_concurrency.helpers

import simulacrum.typeclass

@typeclass trait AtomicRefCounter [R[_], T] {
  def countGets(container: R[T]): Int
}