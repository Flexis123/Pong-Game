package snake.logic

object Util {
  def isInteger(num: Double): Boolean = (num - Math.floor(num)) < 0.000000001

}
