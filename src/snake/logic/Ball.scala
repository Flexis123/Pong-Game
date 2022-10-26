package snake.logic

import snake.logic

case class Ball(position: Point, velocity: Point, velocityIncrement: Point,gridDims: Dimensions){

  def move(): Ball = {
    var newPos = position + velocity

    if(newPos.x < 1) newPos = Point(1f, newPos.y)
    if(newPos.y < 1) newPos = Point(newPos.x, 1f)
    if(newPos.x >= gridDims.width) newPos = Point((gridDims.width - 1).toFloat, newPos.y)
    if(newPos.y >= gridDims.height) newPos = Point(newPos.x, (gridDims.height - 1).toFloat)

    Ball(newPos, velocity, velocityIncrement, gridDims)
  }

  def collidedWithVerticalWall(gameState: GameState): Boolean = {
    val vertWallCollided = position.x == gridDims.width - 1 || position.x == 0
    !collidedWithPlayer(gameState.playerL) && !collidedWithPlayer(gameState.playerR) && vertWallCollided
  }

  def collidedWithPlayers(gameState: GameState): Boolean = {
    collidedWithPlayer(gameState.playerL) || collidedWithPlayer(gameState.playerR)
  }
  def horizontalWallCollision: Boolean = position.y == gridDims.height - 1 || position.y == 0

  def updateVelocity(gameState: GameState): Ball = {
    var newVelocity = velocity
    var newVelocityIncrement = velocityIncrement

    if(horizontalWallCollision) newVelocity =  Point(newVelocity.x, -newVelocity.y)
    else if(collidedWithPlayers(gameState)) {
      newVelocity = Point(-newVelocity.x, -newVelocity.y)
      newVelocity = velocity + velocityIncrement
    }

    Ball(position, newVelocity, velocityIncrement, gridDims)
  }
  private def collidedWithPlayer(player: Player): Boolean = player.body.map(p => p + Point(1, 0)).contains(position)
}