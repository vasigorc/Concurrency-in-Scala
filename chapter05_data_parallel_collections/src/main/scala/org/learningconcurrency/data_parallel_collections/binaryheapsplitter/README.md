## Binary Heap Splitter

> Exercise 7. Implement a custom splitter for the binary heap data structure.

Because of the following requirement of 
[Scala Splitters](https://www.scala-lang.org/api/2.12.13/scala/collection/parallel/SeqSplitter.html)
I've decided to re-use existing scalaz Heap implementation as the target for
the splitter. With that after iterator is called the first time the initial 
data-structure is not traversed:

> ...a splitter is not allowed to copy large parts of the collection when 
> split; if it did, the computational overhead from splitting would overcome 
> any benefits from parallelization and become a serial bottleneck
> *Learning Concurrent Programming in Scala, Second Edition. p. 177*