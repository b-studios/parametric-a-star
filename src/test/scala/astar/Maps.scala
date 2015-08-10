package astar
package test

object Maps {

  private val ░ = true
  private val █ = false

  trait GameMap {
    def height: Int
    def width: Int
    def apply(x: Int, y: Int): Boolean
  }

  // Map: 10 x 10
  object map1 extends GameMap {

    val height = 10
    val width  = 10

    private val data = Array(

    // x: 0 1 2 3 4 5 6 7 8 9   // y:
    Array(░,░,░,░,░,░,░,░,░,░), // 0
    Array(░,░,░,░,░,░,░,░,░,░), // 1
    Array(█,█,█,░,█,█,█,█,░,░), // 2
    Array(█,█,░,░,█,░,█,░,░,░), // 3
    Array(░,░,░,░,█,░,█,░,░,░), // 4
    Array(█,█,█,█,█,█,█,░,░,░), // 5
    Array(░,░,█,░,░,░,░,░,░,░), // 6
    Array(░,░,█,░,█,█,█,█,█,█), // 7
    Array(░,░,█,░,░,░,░,░,░,░), // 8
    Array(░,░,░,░,░,░,░,░,░,░)) // 9

    def apply(x: Int, y: Int) = data(y)(x)
  }

}
