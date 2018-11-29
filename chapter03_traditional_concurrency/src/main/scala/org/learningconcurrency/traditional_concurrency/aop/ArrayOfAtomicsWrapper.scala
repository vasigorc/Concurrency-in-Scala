package org.learningconcurrency.traditional_concurrency.aop

trait ArrayOfAtomicsWrapper [T]{

  def get(index: Int): T
}
