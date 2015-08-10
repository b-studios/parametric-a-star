package astar

// Protocol:
// ---------
// s1 ~ s2  ==>  hash(s1) == hash(s2)
// Usually one would define (~) = (==)
//
// It is assumed that invalid states are sinks
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
