package astar
package test

import org.scalatest._
import org.scalatest.matchers._

class AStarTest extends FunSpec with Matchers {

  // x, y, rotation (0, 1, 2, 3)
  type State = (Int, Int, Int)

  trait Command
  case object Left extends Command
  case object Right extends Command
  case object Up extends Command
  case object Down extends Command
  case object RotateLeft extends Command
  case object RotateRight extends Command

  // Map: 5 x 8
  val width  = 5
  val height = 8
  val blocked = List((0, 1), (1, 1), (2, 1), (3, 1))

  val engine = new astar.Engine[State, Command] {
    def valid(self: State): Boolean = self match {
      case (x, y, r) =>
        x >= 0 && x < width &&
        y >= 0 && y < height && !(blocked contains (x, y))
    }
    def bisimilar(self: State, other: State): Boolean =
      self == other

    def hash(self: State): Int = self.hashCode

    def transition(state: State, cmd: Command): State = (state, cmd) match {
      case ((x, y, r), Left)         => (x - 1, y, r)
      case ((x, y, r), Right)        => (x + 1, y, r)
      case ((x, y, r), Up)           => (x, y - 1, r)
      case ((x, y, r), Down)         => (x, y + 1, r)
      case ((x, y, r), RotateRight)  => (x, y, (r + 1) % 4)
      case ((x, y, r), RotateLeft)   => (x, y, (r - 1) % 4)
    }
    def commands = List(Left, Right, Up, Down, RotateLeft, RotateRight)
    def distance(fst: State, other: State): Double = heuristic(fst, other)
  }

  // Here: Manhattan distance that also considers rotation
  def heuristic(from: State, to: State): Double = (from, to) match {
    case ((x1, y1, r1), (x2, y2, r2)) =>
      math.abs(x1 - x2) + math.abs(y1 - y2) + math.abs(r1 - r2)
  }

  val astarSolver = AStar[State, Command](
    (0, 0, 0),
    (1, 3, 2),
    engine
  )

  astarSolver.computePath shouldBe
    Some(List(RotateRight, RotateRight, Right, Right, Right, Right, Down, Down, Down, Left, Left, Left))

}
