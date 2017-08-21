package vhky.algorithm.despeckle

import vhky.algorithm.adjacent
import vhky.algorithm.data.ImageData

/**
 * Algorithm described in 10.1109/WCSE.2009.718.
 *
 * Hugely consume time when the image is large.
 *
 * Created at 19:02 2017/8/8
 * @author VHKY
 */

object EdgePreservingFilter
{
	fun process(data : ImageData, iterationTime : Int, h : Double) : ImageData
	{
		val start = System.currentTimeMillis()
		println("EPF Starts: 0 / $iterationTime")
		var f = data.copy()
		repeat(iterationTime)
		{
			val _f = f.copy()
			_f.forEachXY()
			{
				var sum = 0.0
				_f[it] = adjacent(it).map { w(f, it, h).apply { sum += this } * f[it] }.reduce { acc, d ->  acc + d } / sum
			}
			f = _f
			println("EPF Progress: ${it + 1} / $iterationTime, ${System.currentTimeMillis() - start}ms")
		}
		return f
	}
	private fun gx(data : ImageData, x : Int, y : Int) = (data[x + 1, y] - data[x - 1, y]) / 2
	private fun gy(data : ImageData, x : Int, y : Int) = (data[x, y + 1] - data[x, y - 1]) / 2
	private fun d(data : ImageData, x : Int, y : Int) = Math.hypot(gx(data, x, y), gy(data, x, y))
	private fun w(data : ImageData, point : Pair<Int, Int>, h : Double) = Math.exp(-Math.sqrt(d(data, point.first, point.second) / (2 * h * h)))
}