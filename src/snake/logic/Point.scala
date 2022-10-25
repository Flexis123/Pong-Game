package snake.logic

// you can alter this file!

case class Point(x : Double, y : Double){
  def +(point: Point): Point = Point(x + point.x, y + point.y)
  def -(point: Point): Point = Point(x - point.x, y - point.y)

  def ==(point: Point): Boolean =
    Math.floor(point.x) == Math.floor(x) &&
      Math.floor(point.y) == Math.floor(y)
}
