package astar

import java.util.{ List => JavaList }

trait JEngine[State, Command] {

  // State related
  def valid(self: State): Boolean
  def bisimilar(self: State, other: State): Boolean
  def hash(self: State): Int
  def transition(state: State, cmd: Command): State

  // Available commands
  def commands: JavaList[Command]

  // Heuristics
  def distance(fst: State, snd: State): Double
}
