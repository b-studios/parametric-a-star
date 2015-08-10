package astar
package test

import org.scalatest._
import org.scalatest.matchers._

import TwoDimensionEngine._

// Augments the 2d engine by new commands for diagonal movements
object TwoDimensionDiagonalEngine {

  case object UpLeft    extends Command
  case object UpRight   extends Command
  case object DownLeft  extends Command
  case object DownRight extends Command

  trait DiagonalEngine extends Engine {

    override def transition(state: Point, cmd: Command): Point = (state, cmd) match {
      case ((x, y), UpLeft)    => (x - 1, y - 1)
      case ((x, y), UpRight)   => (x + 1, y - 1)
      case ((x, y), DownLeft)  => (x - 1, y + 1)
      case ((x, y), DownRight) => (x + 1, y + 1)
      case _                   => super.transition(state, cmd)
    }
    override def commands =
      List(UpLeft, UpRight, DownLeft, DownRight) ++ super.commands
  }
  object DiagonalEngine extends DiagonalEngine
}

class TwoDimensionDiagonalTest extends FunSpec with Matchers {

  import TwoDimensionDiagonalEngine._

  describe("From (0, 0) to (0, 4)") {
    val result = AStar((0, 0), (0, 4), DiagonalEngine).computePath
    result shouldBe Some(List(
      DownRight, Right, DownRight,
      DownLeft, DownLeft, Left))
  }

  describe("From (0, 0) to (5, 3)") {
    val result = AStar((0, 0), (5, 3), DiagonalEngine).computePath
    result shouldBe None
  }

  describe("From (0, 0) to (7, 3)") {
    val result = AStar((0, 0), (7, 3), DiagonalEngine).computePath
    result shouldBe Some(List(
      DownRight, Right,
      DownRight, UpRight,
      Right, Right, Right,
      DownRight, DownLeft))
  }

  describe("From (0, 0) to (1, 6)") {
    val result = AStar((0, 0), (1, 6), DiagonalEngine).computePath
    result shouldBe Some(List(
      DownRight, Right,
      DownRight, UpRight,
      Right, Right, Right,
      DownRight, DownLeft,
      Down, Down,
      DownLeft, Left, Left,
      DownLeft, Down,
      DownLeft, UpLeft,
      Up, Up))
  }
}
