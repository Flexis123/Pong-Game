package snake.logic

// you can alter this file!

sealed abstract class CellType
case object PlayerBody extends CellType
case object BallBody extends CellType
case object Empty extends CellType