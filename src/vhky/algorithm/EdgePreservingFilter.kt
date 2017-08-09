package vhky.algorithm

import vhky.algorithm.data.ImageData
import vhky.algorithm.data.color.asGray

/**
 * No Description
 *
 * Created at 19:02 2017/8/8
 * @author VHKY
 */

object EdgePreservingFilter
{
	fun process(data : ImageData, iterationTime : Int, h : Double) : ImageData
	{
		println("EPF Starts")
		var f = data.copy()
		repeat(iterationTime)
		{
			val _f = f.copy()
			_f.forEachXY()
			{
				var sum = 0.0
				_f[it] = adjacent(it).map { w(f, it, h).apply { sum += this } * f[it].asGray }.reduce { acc, d ->  acc + d } / sum
			}
			f = _f
			println("EPF Progress: ${it + 1} / $iterationTime")
		}
		return f
	}
	private fun gx(data : ImageData, x : Int, y : Int) = (data[x + 1, y].asGray - data[x - 1, y].asGray) / 2
	private fun gy(data : ImageData, x : Int, y : Int) = (data[x, y + 1].asGray - data[x, y - 1].asGray) / 2
	private fun d(data : ImageData, x : Int, y : Int) = Math.hypot(gx(data, x, y), gy(data, x, y))
	private fun w(data : ImageData, point : Pair<Int, Int>, h : Double) = Math.exp(-Math.sqrt(d(data, point.first, point.second) / (2 * h * h)))
}