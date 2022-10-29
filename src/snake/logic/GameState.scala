package snake.logic

import engine.GameBase

sealed abstract class GameStatus{
  def next(gameState: GameState): GameStatus
}

case class ReadyUpStage() extends GameStatus{
  override def next(gameState: GameState): GameStatus = {
    if(gameState.playerR.isReady && gameState.playerL.isReady) CountdownStage()
    else this
  }
}

case class CountdownStage(var count: Int = GameLogic.CountdownSeconds) extends GameStatus{
  override def next(gameState: GameState): GameStatus = {
    Thread.sleep(1000)
    count -= 1

    if(count < 0) Ongoing()
    else this
  }
}

case class Ongoing(canSpawnNewBall: Boolean = false) extends GameStatus{
  override def next(gameState: GameState): GameStatus = {
    if(gameState.playerR.score == GameLogic.ScoreForWin ||
      gameState.playerL.score == GameLogic.ScoreForWin) {
      GameOver()
    }else if(gameState.ball.hasCollidedWithVerticalWall(gameState)) OngoingNewBall()
    else Ongoing()
  }
}


case class OngoingNewBall() extends GameStatus{

  // hate inner classes
  private val gb = new GameBase()
  private lazy val timer = {
    val t = new gb.UpdateTimer(2)
    t.init()
    t
  }
  override def next(gameState: GameState): GameStatus = {
    if(timer.timeForNextFrame()) Ongoing(true)
    else this
  }

}


case class GameOver() extends GameStatus{
  override def next(gameState: GameState): GameStatus = this
}


case class GameState(playerL: Player, playerR: Player, ball: Ball, stepNum: Long, gameStatus: GameStatus) {

  def leader: Player = if(playerL.score > playerR.score) playerL else playerR

  def next(playerL: Player, playerR: Player, ball: Ball): GameState = {
    val next = GameState(playerL, playerR, ball, stepNum + 1, null)
    val nextStatus = gameStatus.next(next)
    GameState(playerL, playerR, ball, stepNum + 1, nextStatus)
  }
}

object GameState{
  def apply(playerL: Player, playerR: Player, ball: Ball): GameState =
    GameState(playerL, playerR, ball, 0, ReadyUpStage())

}
