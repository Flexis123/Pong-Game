// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package snake.game


import engine.GameBase
import engine.graphics.Color._
import engine.graphics.{Color, Point, Rectangle}
import engine.random.ScalaRandomGen
import processing.core.PApplet
import processing.event.KeyEvent
import snake.game.PongGame.{HeightCellInPixels, WidthCellInPixels}
import snake.logic._

import java.awt.event
import java.awt.event.KeyEvent._


class PongGame extends GameBase {

  var gameLogic = new GameLogic(new ScalaRandomGen(),GameLogic.DefaultGridDims).init()
  val updateTimer = new UpdateTimer(GameLogic.FramesPerSecond.toFloat)
  val gridDimensions : Dimensions =  gameLogic.gridDims
  val widthInPixels: Int = (GameLogic.DrawSizeFactor * WidthCellInPixels * gridDimensions.width).ceil.toInt
  val heightInPixels: Int = (GameLogic.DrawSizeFactor *  HeightCellInPixels * gridDimensions.height).ceil.toInt
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  val middleX: Float = (widthInPixels - 1).toFloat / 2.toFloat
  val halfMiddleX: Float = middleX / 2f
  val lScorePos: Point = Point(middleX - halfMiddleX, 8f)
  val rScorePos: Point = Point(middleX + halfMiddleX, 8f)

  // this function is wrongly named draw by processing (is called on each update next to drawing)
  override def draw(): Unit = {
    val status = gameLogic.gameState.gameStatus
    drawGrid()
    status match {
      case ReadyUpStage() => drawGameStartScreen()
      case CountdownStage(count) => drawCountdown(count)
      case GameOver() => drawGameOverScreen()
      case _ => ()
    }
    updateState()
  }

  def drawCountdown(count: Int): Unit = {
    setFillColor(Color.White)
    drawTextCentered(count.toString, 20, screenArea.center)
  }

  def drawScore(pos: Point, player: Player): Unit = {
    setFillColor(Color.White)
    drawText(player.score.toString, pos, withShadow = false)
  }

  def drawGameOverScreen(): Unit = {
    setFillColor(Red)
    drawTextCentered(
      "GAME OVER!\n " + gameLogic.gameState.leader.name + " has won" +"\npress 'r' to restart",
      20,
      screenArea.center
    )
  }

  def drawGameStartScreen(): Unit = {
    setFillColor(Color.White)

    val lPlayer = gameLogic.gameState.playerL
    val rPlayer = gameLogic.gameState.playerR

    val text = buildPlayerReadyString(lPlayer) + buildPlayerReadyString(rPlayer)

    drawTextCentered(text, size = 20, screenArea.center)
  }

  def buildPlayerReadyString(player: Player): String =
   player.name + " ready: " + player.isReady + "\n"

  def drawGrid(): Unit = {

    val widthPerCell = screenArea.width / gridDimensions.width
    val heightPerCell = screenArea.height / gridDimensions.height

    def getCell(p : snake.logic.Point): Rectangle = {
      val leftUp = Point(screenArea.left + p.x * widthPerCell,
        screenArea.top + p.y * heightPerCell)
      Rectangle(leftUp, widthPerCell, heightPerCell)
    }

    def drawCell(area: Rectangle, cell: CellType): Unit = {
      cell match {
        case PlayerBody =>
          setFillColor(Color.White)
          drawRectangle(area)
        case BallBody =>
          setFillColor(Color.Gray)
          drawEllipse(area)
        case _ =>
          setFillColor(Color.Black)
          drawRectangle(area)
      }
    }

    setFillColor(Color.Black)
    drawRectangle(screenArea)

    for (p <- gridDimensions.allPointsInside) {
      drawCell(getCell(p), gameLogic.getCellType(p))
    }

    drawScore(lScorePos, gameLogic.gameState.playerL)
    drawScore(rScorePos, gameLogic.gameState.playerR)

  }

  private def getPlayerId(event: KeyEvent): Int = {
    event.getKeyCode match {
      case VK_W | VK_S | VK_1 => GameLogic.LeftPlayerId
      case VK_UP | VK_DOWN | VK_0 => GameLogic.RightPlayerId
      case _ => 0
    }
  }

  private def startStopMove(event: KeyEvent, startMove: Boolean): Unit = {
    event.getKeyCode match {
      case VK_W | VK_UP =>
        gameLogic.moveUp(getPlayerId(event), startMove)
      case VK_S | VK_DOWN =>
        gameLogic.moveDown(getPlayerId(event), startMove)

      case _ => ()
    }

  }
  /** Method that calls handlers for different key press events.
   * You may add extra functionality for other keys here.
   * See [[event.KeyEvent]] for all defined keycodes.
   *
   * @param event The key press event to handle
   */

  override def keyPressed(event: KeyEvent): Unit = {
    event.getKeyCode match {
      case VK_0 | VK_1 => gameLogic.setReady(getPlayerId(event))
      case VK_W | VK_S | VK_DOWN | VK_UP => startStopMove(event, startMove = true)
      case VK_R => gameLogic = gameLogic.init()
      case _ => ()
    }
  }

  override def keyReleased(event: KeyEvent): Unit = startStopMove(event, startMove = false)

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(widthInPixels, heightInPixels)
  }

  override def setup(): Unit = {
    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)
    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()
  }


  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      gameLogic.step()
      updateTimer.advanceFrame()
    }
  }

}


object PongGame {


  val WidthCellInPixels: Double = 20 * GameLogic.DrawSizeFactor
  val HeightCellInPixels: Double = WidthCellInPixels

  def main(args: Array[String]): Unit = {
    // This is needed for Processing, using the name
    // of the class in a string is not very beautiful...
    PApplet.main("snake.game.PongGame")
  }

}
