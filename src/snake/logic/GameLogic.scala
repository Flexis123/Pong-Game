package snake.logic

import engine.random.RandomGenerator
import snake.logic.GameLogic._


/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions){

  private var playerL: Player = null
  private var playerR: Player = null
  private var ball: Ball = null
  var gameState: GameState = null

  private def updateGameState(): Unit =
    gameState = gameState.next(playerL, playerR, ball)

  private def movePlayers(): Unit = {
    playerL = playerL.move()
    playerR = playerR.move()
  }
  def step(): Unit = {
    gameState.gameStatus match{
      case Ongoing(canSpawnNewBall) =>

        if(canSpawnNewBall) {
          if (ball.position.x <= 0) playerR = playerR.incrementScore()
          else playerL = playerL.incrementScore()
          ball = spawnNewBall()
        }
        else ball = ball.move(gameState)

        movePlayers()

      case OngoingNewBall() => movePlayers()
      case _ => ()
    }

    updateGameState()
  }

  private def updatePlayer(playerId: Int, doIfPresent: Player => Player): Unit = {
    playerId match {
      case LeftPlayerId => playerL = doIfPresent(playerL)
      case RightPlayerId => playerR = doIfPresent(playerR)
      case _ => ()
    }
    updateGameState()
  }

  def setReady(playerId: Int): Unit = updatePlayer(playerId, p => p.setAsReady())

  def moveUp(playerId: Int, mvu: Boolean): Unit = updatePlayer(playerId, p => p.setMovingUp(mvu))

  def moveDown(playerId: Int, mvd: Boolean): Unit = updatePlayer(playerId, p => p.setMovingDown(mvd))

  def spawnNewBall(): Ball = {
    val pos = Point((gridDims.width - 1) / 2, random.randomInt(gridDims.height))
    Ball(pos, BallVelocity, gridDims)
  }

  def init(): GameLogic = {
    playerL = Player(LeftPlayerName, PlayerLength, 0, gridDims)
    playerR = Player(RightPlayerName, PlayerLength, gridDims.width - 1, gridDims)
    ball = spawnNewBall()

    gameState = GameState(playerL, playerR, ball)
    this
  }

  def getCellType(p: Point): CellType = {
    if(playerL.body.contains(p) || playerR.body.contains(p)) PlayerBody
    else if(ball.position == p) BallBody
    else Empty
  }
}

/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 60// change this to increase/decrease speed of game

  val PlayerLength: Int = 5
  val LeftPlayerName = "Left Player"
  val RightPlayerName = "Right Player"
  val LeftPlayerId = 1
  val RightPlayerId = 2
  val BallVelocity: Point = Point(1, 1)
  val ScoreForWin = 9
  val CountdownSeconds = 3

  val DrawSizeFactor = 1
  val DefaultHeight = 32
  val DefaultWidth = (DefaultHeight * 1.5).toInt
  val DefaultGridDims
    : Dimensions =
    Dimensions(width = DefaultWidth, height = DefaultHeight)  // you can adjust these values to play on a different sized board

}


