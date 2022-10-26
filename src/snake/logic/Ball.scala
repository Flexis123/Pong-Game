package snake.logic

import snake.logic

case class Ball(position: Point, velocity: Point,gridDims: Dimensions){

  def move(gameState: GameState): Ball = {
    var newBall = this
    if(gameState.stepNum % 2 == 0){
      var newVelocity = velocity

      if (hasCollidedWithHorizontalWall) newVelocity = Point(newVelocity.x, -newVelocity.y)
      if (hasCollidedWithPlayers(gameState)) newVelocity = Point(-newVelocity.x, newVelocity.y)

      newBall = Ball(position + newVelocity, newVelocity, gridDims)
    }
    newBall
  }

  def hasCollidedWithPlayers(gameState: GameState): Boolean =
    gameState.playerL.body.contains(position) || gameState.playerR.body.contains(position)

  def hasCollidedWithVerticalWall(gameState: GameState): Boolean = {
    val vertWallCollided = position.x > gridDims.width - 1 || position.x < 0
    !hasCollidedWithPlayers(gameState) && vertWallCollided
  }
  def hasCollidedWithHorizontalWall: Boolean = position.y > gridDims.height - 1 || position.y < 0
}
