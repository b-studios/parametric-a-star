package astar;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.util.List;
import java.util.Arrays;

import static astar.Command.*;

class Point extends Product {
  public Point(int x, int y) { super(x, y); }
  public final int x() { return field(0); }
  public final int y() { return field(1); }
}

class OrientedPoint extends Product {
  public OrientedPoint(int x, int y, int rotation) { super(x, y, rotation); }
  public final int x() { return field(0); }
  public final int y() { return field(1); }
  public final int rotation() { return field(2); }
}

enum Command {
  Left, Right, Up, Down, RotateLeft, RotateRight
}

public class JAStarTest implements JEngine<OrientedPoint, Command> {

  // Factory methods
  static Point Point(int x, int y) { return new Point(x, y); }
  static OrientedPoint OrientedPoint(int x, int y, int rotation) { return new OrientedPoint(x, y, rotation); }

  // Map
  final int width  = 5;
  final int height = 8;
  final List<Point> blocked = Arrays.asList(
      Point(0, 1), Point(1, 1), Point(2, 1), Point(3, 1));

  // `Engine` implementation
  public boolean valid(OrientedPoint p) {
    return
      p.x() >= 0 && p.x() < width &&
      p.y() >= 0 && p.y() < height &&
      !(blocked.contains(Point(p.x(), p.y())));
  }
  public boolean bisimilar(OrientedPoint self, OrientedPoint other) {
    return self.equals(other);
  }
  public int hash(OrientedPoint p) {
    return p.hashCode();
  }
  public OrientedPoint transition(OrientedPoint p, Command cmd) {
    switch (cmd) {
      case Left:        return OrientedPoint(p.x() - 1, p.y(), p.rotation());
      case Right:       return OrientedPoint(p.x() + 1, p.y(), p.rotation());
      case Up:          return OrientedPoint(p.x(), p.y() - 1, p.rotation());
      case Down:        return OrientedPoint(p.x(), p.y() + 1, p.rotation());
      case RotateRight: return OrientedPoint(p.x(), p.y(), (p.rotation() + 1) % 4);
      case RotateLeft:  return OrientedPoint(p.x(), p.y(), (p.rotation() - 1) % 4);
      default: return null;
    }
  }
  public List<Command> commands() {
    return Arrays.asList(Left, Right, Up, Down, RotateLeft, RotateRight);
  }
  public double distance(OrientedPoint p, OrientedPoint q) {
    return
      Math.abs(p.x() - q.x()) +
      Math.abs(p.y() - q.y()) +
      Math.abs(p.rotation() - q.rotation());
  }

  @Test
  public void computePathTest() {
    List<Command> result = new JAStar<OrientedPoint, Command>(
      OrientedPoint(0, 0, 0),
      OrientedPoint(1, 3, 2),
      this).computePath();

    assertThat(result, hasItems(RotateRight, Right, Down, Left));
    assertThat(result.size(), is(12));
  }
}
