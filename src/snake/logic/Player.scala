package snake.logic

case class Player(body: Set[Point], griDims: Dimensions , isReady: Boolean,
                  isMovingUp: Boolean, isMovingDown: Boolean, score: Int){

  private def _move(offset: Point): Player = {
    val newBody = body.map(p => p + offset).filter(p => p.y < griDims.height && p.y >= 0)

    if(newBody.size == body.size) Player(newBody, griDims, isReady, isMovingUp, isMovingDown, score)
    else this
  }

  def move(): Player ={
    if(isMovingDown) _move(Point(0, 1))
    else if(isMovingUp) _move(Point(0, -1))
    else this
  }

  def setMovingUp(mvu: Boolean): Player =
    Player(body, griDims, isReady, isMovingUp = mvu, isMovingDown, score)

  def setMovingDown(mvd: Boolean): Player =
    Player(body, griDims, isReady, isMovingUp, isMovingDown = mvd, score)

  def setAsReady(): Player = Player(body, griDims, isReady = true, isMovingUp, isMovingDown, score)

  def incrementScore(): Player = Player(body, griDims, isReady, isMovingUp, isMovingDown, score + 1)
}

object Player{
  def apply(length: Int, spawnX: Int, griDims: Dimensions): Player = {
    var startPoint =  Point(spawnX, griDims.height / 2 - length)
    var body: Set[Point] = Set()

    for (_ <- 0 until length){
      body = body + startPoint
      startPoint = startPoint + Point(0, -1)
    }
    Player(body, griDims, isReady = false, isMovingUp = false, isMovingDown = false, 0)
  }


}
