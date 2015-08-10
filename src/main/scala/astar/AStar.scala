package astar

import scala.collection.mutable

// Design goals:
// - parametric to adapt to many use cases
// - interface interoperable with Java

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

trait Domain[State, Command] {

  // Typeclasses to be implemented by the user
  val engine: Engine[State, Command]

  private[astar]
  implicit class StateOps[S <% State](self: S) {
    def valid: Boolean = engine.valid(self)
    def ~(other: State): Boolean = engine.bisimilar(self, other)
    def hash: Int = engine.hash(self)
    def apply(cmd: Command): State = engine.transition(self, cmd)
    def <>(other: State): Double = engine.distance(self, other)
  }
}

// A parametric path finding algorithm based on
// bisimilarity of states.
case class AStar[State, Command](
  start: State,
  goal: State,
  engine: Engine[State, Command]) extends Domain[State, Command] { astar =>

  val statespace = StateSpace(astar)
  import engine._
  import statespace._

  private val closed = mutable.Set.empty[Node]
  private val open   = mutable.PriorityQueue.empty[Node]

  def computePath: Option[List[Command]] = {
    closed.clear()
    open.clear()

    if (!start.valid || !goal.valid)
      return None

    val startNode = getOrElseSpawn(start)
    val goalNode  = getOrElseSpawn(goal)

    open enqueue startNode

    while (open.nonEmpty) {
      val current = open.dequeue()

      if (current == goalNode)
        return Some(current.path.get.reverse)

      closed add current

      for (
        (cmd, neighbour) <- current.neighbours
        if neighbour.valid
        if !(closed contains neighbour)
      ) {
        // XXX fix performance here?
        if (!(open.toSet contains neighbour)) {
          open enqueue neighbour
          neighbour.path = cmd :: current.path.get
        }
      }
    }
    None
  }
}

private[astar]
case class StateSpace[State, Command](
  solver: AStar[State, Command]
) {
  import solver._
  import engine._

  // Ordered: The better (distance to goal), the larger.
  // goal should be Top of the lattice
  case class Node(state: State) extends Ordered[Node]  {
    // what was previously called "neighbours" are now follow-
    // states reached by transitioning with all available
    // commands.
    lazy val neighbours: Map[Command, Node] =
      commands.zip(commands map { c => getOrElseSpawn(state(c)) }).toMap

    // the path back to the start node
    var _path: Option[List[Command]] = Some(Nil)
    def path = _path
    def path_=(p: List[Command]): this.type = {
      _path = Some(p); this
    }

    def compare(that: Node) = {
      val dist = (state <> goal) - (that.state <> goal)

      if (dist < 0) 1
      else if (dist == 0) 0
      else -1
    }
  }

  // lift StateOps to Node
  implicit def stateOfNode(n: Node): State = n.state

  class Key(val state: State) {
    override def equals(other: Any): Boolean =
      other.asInstanceOf[Key].state ~ state
    override lazy val hashCode: Int =
      state.hash
  }

  // The map is indexed by the hash-value of states
  val nodes = mutable.HashMap.empty[Key, Node]
  def get(s: State): Option[Node] = nodes.get(new Key(s))
  def update(s: State, n: Node) = nodes.update(new Key(s), n)
  def getOrElseSpawn(s: State): Node =
      nodes.getOrElseUpdate(new Key(s), Node(s))
}
