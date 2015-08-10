package astar

import java.util.{ List => JavaList }
import scala.collection.JavaConverters._

class JAStar[State, Command](
  start: State,
  goal: State,
  engine: JEngine[State, Command]) {

  val scalaEngine = new Engine[State, Command] {
    def valid(self: State) = engine.valid(self)
    def bisimilar(self: State, other: State) = engine.bisimilar(self, other)
    def hash(self: State) = engine.hash(self)
    def transition(state: State, cmd: Command) = engine.transition(state, cmd)
    lazy val commands: List[Command] = engine.commands.asScala.toList
    def distance(fst: State, snd: State) = engine.distance(fst, snd)
  }

  def computePath: JavaList[Command] =
    AStar(start, goal, scalaEngine).computePath match {
      case Some(l) => l.asJava
      case None => null
    }
}
