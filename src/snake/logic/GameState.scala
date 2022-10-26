package snake.logic

case class GameState(playerL: Player, playerR: Player, ball: Ball, stepNum: Long){
  def gameOver: Boolean = ball.hasCollidedWithVerticalWall(this)
  def canStart: Boolean = playerL.isReady && playerR.isReady
}

object GameState{
  def apply(playerL: Player, playerR: Player, ball: Ball): GameState =
    GameState(playerL, playerR, ball, 0)
}