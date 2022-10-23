// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package snake.game

import engine.GameBase
import engine.graphics.Color._
import engine.graphics.{Color, Point, Rectangle}
import engine.random.ScalaRandomGen
import processing.core.PApplet
import processing.event.KeyEvent

import java.awt.event.KeyEvent._
import snake.game.PongGame._
import snake.logic.{BallBody, CellType, Dimensions, GameLogic, PlayerBody, Point => GridPoint}

import java.awt.event

class PongGame extends GameBase {

  var gameLogic = new GameLogic(new ScalaRandomGen(),GameLogic.DefaultGridDims)
  val updateTimer = new UpdateTimer(GameLogic.FramesPerSecond.toFloat)
  val gridDimensions : Dimensions =  gameLogic.gridDims
  val widthInPixels: Int = (GameLogic.DrawSizeFactor * WidthCellInPixels * gridDimensions.width).ceil.toInt
  val heightInPixels: Int = (GameLogic.DrawSizeFactor *  HeightCellInPixels * gridDimensions.height).ceil.toInt
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  // this function is wrongly named draw by processing (is called on each update next to drawing)
  override def draw(): Unit = {
    drawGrid()
    if (gameLogic.canStart) updateState()
    else drawGameStartScreen()

    if (gameLogic.gameOver) drawGameOverScreen()
  }

  def drawGameOverScreen(): Unit = {
    setFillColor(Red)
    drawTextCentered("GAME OVER!", 20, screenArea.center)
  }

  def drawGameStartScreen(): Unit = {
    setFillColor(Color.White)

    var text = "Player left ready: " + gameLogic.playerL.isReady + "\n"
    text += "Player right ready: " + gameLogic.playerR.isReady

    drawTextCentered(text, size = 20, screenArea.center)
  }

  def drawGrid(): Unit = {

    val widthPerCell = screenArea.width / gridDimensions.width
    val heightPerCell = screenArea.height / gridDimensions.height

    def getCell(p : GridPoint): Rectangle = {
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

  }

  private def getPlayerId(event: KeyEvent): Int = {
    event.getKeyCode match {
      case VK_W | VK_S | VK_1 => 1
      case VK_UP | VK_DOWN | VK_0 => 2
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
