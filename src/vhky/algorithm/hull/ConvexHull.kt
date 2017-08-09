package vhky.algorithm.hull

/**
 * No Description
 *
 * Created at 10:33 2017/8/4
 * @author VHKY
 */
class ConvexHull
{
	companion object
	{
		fun get(points : List<Pair<Double, Double>>) : Unit
		{
			if (points.size < 3) return Unit
			val min = Math.floor(points.minBy { it.first }!!.first).toInt()
			val max = Math.ceil(points.maxBy { it.first }!!.first).toInt()
		}
		private class Bucket(private val min : Int, max : Int)
		{
			val data = IntArray(max - min + 1)
			operator fun get(x : Double) = x to data[(x - min).toInt()]
			
		}
		private enum class Direction{ Left, Right, SameLine }
		private fun judgeTurnDirection(point1 : Pair<Double, Double>, point2 : Pair<Double, Double>, point3 : Pair<Double, Double>) : Direction
		{
			val determinant = (point2.first - point1.first) * (point3.second - point2.second) - (point2.second - point1.second) * (point3.first - point2.first)
			return when
			{
				determinant > 0 -> Direction.Left
				determinant < 0 -> Direction.Right
				else -> Direction.SameLine
			}
		}
	}
	
}