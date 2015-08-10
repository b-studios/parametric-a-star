package astar;

import java.util.Arrays;

/**
 * Instances of this class represent a product (in the sense of
 * type theory).
 *
 * For instance a 3-tuple can be defined as:
 *
 *     class Tuple3<A, B, C> extends Product {
 *       public Tuple3(A _1, B _2, C _3) { super(_1, _2, _3); }
 *       public final A _1() { return field(0); }
 *       public final B _2() { return field(1); }
 *       public final C _3() { return field(2); }
 *     }
 *
 * Defines structural equality and hashCode in terms of the
 * fields supplied to the super constructor.
 */
public class Product {
  private final Object[] fields;
  private final Class cls = getClass();
  protected Product(Object... values) {
    this.fields = values;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (other.getClass() != cls) return false;
    return Arrays.equals(((Product) other).fields, fields);
  }

  @SuppressWarnings("unchecked")
  protected final <E> E field(int n) { return (E) fields[n]; }

  @Override
  public int hashCode() { return Arrays.hashCode(fields); }

  @Override
  public String toString() {
    String out = cls.getName() + "(";
    for (int i = 0; i < fields.length; i++) {
      if (i != 0) out += ", ";
      out += fields[i].toString();
    }
    return out + ")";
  }
}
