package astar
package test

import org.scalatest._
import org.scalatest.matchers._

object TwoDimensionEngine {

  // x, y
  type Point = (Int, Int)

  trait Command
  case object Left extends Command
  case object Right extends Command
  case object Up extends Command
  case object Down extends Command

  trait Engine extends astar.Engine[Point, Command] {
    def valid(self: Point): Boolean = self match {
      case (x, y) =>
        x >= 0 && x < Maps.map1.width &&
        y >= 0 && y < Maps.map1.height &&
        Maps.map1(x, y)
    }
    def bisimilar(self: Point, other: Point): Boolean =
      self == other

    def hash(self: Point): Int = self.hashCode

    def transition(state: Point, cmd: Command): Point = (state, cmd) match {
      case ((x, y), Left)  => (x - 1, y)
      case ((x, y), Right) => (x + 1, y)
      case ((x, y), Up)    => (x, y - 1)
      case ((x, y), Down)  => (x, y + 1)
    }
    def commands = List(Left, Right, Up, Down)
    def distance(fst: Point, snd: Point): Double = manhattan(fst, snd)
  }
  object Engine extends Engine

  def manhattan(from: Point, to: Point): Double = (from, to) match {
    case ((x1, y1), (x2, y2)) => math.abs(x1 - x2) + math.abs(y1 - y2)
  }
}

class TwoDimensionTest extends FunSpec with Matchers {

  import TwoDimensionEngine._

  describe("From (0, 0) to (0, 4)") {
    val result = AStar((0, 0), (0, 4), Engine).computePath
    result shouldBe Some(List(
      Down, Right, Right, Right,
      Down, Down,
      Left, Down,
      Left, Left))
  }

  describe("From (0, 0) to (5, 3)") {
    val result = AStar((0, 0), (5, 3), Engine).computePath
    result shouldBe None
  }

  describe("From (0, 0) to (7, 3)") {
    val result = AStar((0, 0), (7, 3), Engine).computePath
    result shouldBe Some(List(
      Right, Right, Right, Right, Right, Right, Right,
      Down, Right,
      Down, Down,
      Left))
  }

  describe("From (0, 0) to (1, 6)") {
    val result = AStar((0, 0), (1, 6), Engine).computePath
    result shouldBe Some(List(
      Right, Down,
      Right, Right, Right, Right, Right, Right, Right,
      Down, Down, Left,
      Down, Down, Down,
      Left, Left, Left, Left,
      Down, Down, Down,
      Left, Left,
      Up, Up, Up))
  }
}
