package snake.logic

// you can alter this file!

case class Point(x : Int, y : Int) {
  def +(point: Point): Point = Point(x + point.x, y + point.y)
  def -(point: Point): Point = Point(x - point.x, y - point.y)
}
