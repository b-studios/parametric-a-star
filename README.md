# Parametric A*
A Scala / Java impementation of A* (A Star) parametric in the possible states and transitions. 

[![Build Status](https://travis-ci.org/b-studios/parametric-a-star.svg?branch=master)](https://travis-ci.org/b-studios/parametric-a-star)

Standard implementations of [A*](https://en.wikipedia.org/wiki/A*_search_algorithm) are defined 
on a 2D map with a fixed set of commands for navigation. In contrast, this implementation is
fully parametric (unaware) of the used "map"-structure and "commands". To guide the search
algorithm the [Engine](https://github.com/b-studios/parametric-a-star/blob/master/src/main/scala/astar/AStar.scala#L15-L28) 
interface has to be implemented, repeated here for convenience:

~~~scala
trait Engine[State, Command] {
  // State related
  def valid(self: State): Boolean
  def bisimilar(self: State, other: State): Boolean
  def hash(self: State): Int
  def transition(state: State, cmd: Command): State

  // Available commands
  def commands: List[Command]

  // Heuristics
  def distance(fst: State, other: State): Double
}
~~~

The common A* algorithm can be recovered, by instantiating `State = (Int, Int)` and 
`Command` with the desired set of commands, whose behaviour is implemented in
`transition`. The map structure then will be implemented by validity of positions
in `valid`. Usually `bisimilar = equals` and `hash = hashCode`. However, also
more complex equalities can be implemented like symmetry of rotations. 

## Usage
For example usages see either [this Scala file](https://github.com/b-studios/parametric-a-star/blob/master/src/test/scala/astar/AStarTest.scala) or [this Java file](https://github.com/b-studios/parametric-a-star/blob/master/src/test/java/astar/JAStarTest.java).
