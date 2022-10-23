package snake.logic

case class Ball(position: Point, velocity: Point, gridDims: Dimensions){
  def move(): Ball = Ball(position + velocity, velocity, gridDims)

  def collidedWithHorizontalWall(): Boolean = false

  def collidedWithVerticalPlayers(): Boolean = false

  def collidedWithPLayer(player: Player): Boolean = false
}
