package snake.logic

case class Velocity(velX: Double, velY: Double){
  def +(rhs: Velocity): Velocity = Velocity(velX + rhs.velX, velY + rhs.velY)
  def +(rhs: Point): Point = Point((rhs.x + velX).toInt, (rhs.y + velY).toInt)
}
