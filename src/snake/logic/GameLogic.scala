package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.logic.GameLogic._

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions){

  var playerL: Player = Player(PlayerLength, 0, gridDims)
  var playerR: Player = Player(PlayerLength, gridDims.width - 1, gridDims)
  private var ball: Ball = null

  def gameOver: Boolean = false

  //ball movement
  def step(): Unit = {
    playerL = playerL.move()
    playerR = playerR.move()
  }

  private def updatePlayer(playerId: Int, doIfPresent: Player => Player): Unit = {
    playerId match {
      case 1 => playerL = doIfPresent(playerL)
      case 2 => playerR = doIfPresent(playerR)
      case _ => ()
    }
  }

  def setReady(playerId: Int): Unit = updatePlayer(playerId, p => p.setAsReady())

  def canStart: Boolean = playerL.isReady && playerR.isReady

  def moveUp(playerId: Int, mvu: Boolean): Unit = updatePlayer(playerId, p => p.setMovingUp(mvu))

  def moveDown(playerId: Int, mvd: Boolean): Unit = updatePlayer(playerId, p => p.setMovingDown(mvd))

  def getCellType(p: Point): CellType = {
    if(playerL.body.contains(p) || playerR.body.contains(p)) PlayerBody
    else Empty
  }

}

/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 30// change this to increase/decrease speed of game

  val PlayerLength: Int = 3
  val BallVelocityOffsetOnPlayerHit = Velocity(0.25, 0.25)
  val BallBaseVelocity = Velocity(1.0, 1.0)

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller

  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultGridDims.width and DefaultGridDims.height
  val DefaultHeight = 25
  val DefaultWidth = (DefaultHeight * 1.65).toInt
  val DefaultGridDims
    : Dimensions =
    Dimensions(width = DefaultWidth, height = DefaultHeight)  // you can adjust these values to play on a different sized board



}


