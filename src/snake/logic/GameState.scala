package snake.logic

case class GameState(playerL: Player, playerR: Player, ball: Ball){
  def gameOver: Boolean = ball.collidedWithVerticalWall(this)
  def canStart: Boolean = playerL.isReady && playerR.isReady
}
