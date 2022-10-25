package snake.logic

case class Ball(position: Point, velocity: Point, gridDims: Dimensions){

  def move(): Ball = {
    val newBall = Ball(position + velocity, velocity, gridDims)
    if(newBall.collidedWithHorizontalWall())
  }

  def collidedWithHorizontalWall(): Boolean = position.x >= gridDims.height - 1 || position.x <= 0

  def collidedWithVerticalWall(): Boolean = position

  def collidedWithPLayer(player: Player): Boolean = false
}
