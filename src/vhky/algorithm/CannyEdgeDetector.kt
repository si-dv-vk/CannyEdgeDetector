package vhky.algorithm

import javafx.scene.image.Image
import vhky.algorithm.data.ImageData
import vhky.algorithm.data.color.Edge
import vhky.algorithm.data.color.GrayScaleFactory
import vhky.algorithm.data.color.asEdge
import vhky.algorithm.data.color.asGray
import java.util.*

/**
 * No Description
 *
 * Created at 9:42 2017/8/3
 * @author VHKY
 */

object CannyEdgeDetector
{
	fun process(image : Image) : Image
	{
		/***
		 * Process:
		 * 1. Convert the image to my data format - Checked
		 * 2. Grey-Scaling - Checked
		 * 3. Gaussian filter
		 * 4. Find intensity gradients
		 * 5. Non-maximum suppression
		 * 6. Double threshold to determine potential edges
		 * 7. Track edge by hysteresis
		 * 8. Finally, Convert the image back - Checked
		 */
		val data = ImageData.fromImage(image, GrayScaleFactory)
		return pipeline(data).toImage()
	}
	private fun pipeline(data : ImageData) : ImageData
	{
		var time = System.currentTimeMillis()
		val temp = preprocess(data)
		println("Preprocess Time = ${(System.currentTimeMillis() - time) / 1000.0} secs")
		time = System.currentTimeMillis()
		val result = postprocess(temp, { it[(it.size * 0.95).toInt()] to it[(it.size * 0.95).toInt()] / 1.5 })
		println("Postprocess Time = ${(System.currentTimeMillis() - time) / 1000.0} secs")
		return result
	}
	class PreprocessResult internal constructor(internal val data : Pair<ImageData, List<Double>>)
	fun preprocess(data : ImageData) : PreprocessResult
	{
		val _data = SpeckleFilter(data)
		var (intensity, direction) = getIntensityGradients(_data)
		intensity = nonMaximumSuppression(intensity, direction)
		return PreprocessResult(intensity to sortEdges(intensity))
	}
	fun postprocess(temp : PreprocessResult, threshold : (List<Double>) -> Pair<Double, Double>) : ImageData
	{
		val _data = temp.data.first.copy()
		doubleThreshold(_data, temp.data.second, threshold)
		trackEdge(_data)
		return _data
	}
	private val GaussianConvolutionKernel = gaussianKernel(15)
	private fun SpeckleFilter(data : ImageData) : ImageData = EdgePreservingFilter.process(data, 5, 3.0)
	private val sqrt2 = Math.sqrt(2.0)
	private val SobelX = ConvolutionKernel(listOf(
			-1.0, 0.0, 1.0,
			-sqrt2 ,0.0, sqrt2,
			-1.0, 0.0, 1.0
	).map { it / (2 + sqrt2) })
	private val SobelY = ConvolutionKernel(listOf(
			1.0, sqrt2, 1.0,
			0.0, 0.0, 0.0,
			-1.0, -sqrt2, -1.0
	).map { it / (2 + sqrt2) })
	private fun getIntensityGradients(data : ImageData) : Pair<ImageData, ImageData>
	{
		val sx = ImageData(data.width, data.height)
		val sy = ImageData(data.width, data.height)
		val intensity = ImageData(data.width, data.height)
		val direction = ImageData(data.width, data.height)
		data.forEachIndexed { index, color -> sx[index] = color; sy[index] = color }
		sx *= SobelX
		sy *= SobelY
		intensity.data.indices.forEach { intensity[it] = GrayScaleFactory.GrayScale(Math.hypot(sy[it].asGray, sx[it].asGray)) }
		direction.data.indices.forEach { direction[it] = GrayScaleFactory.GrayScale(Math.atan2(sy[it].asGray, sx[it].asGray)) }
		return intensity to direction
	}
	private val boundaries = List(4, { Math.PI * (-3.0 / 8.0 + 0.25 * it)})
	private fun nonMaximumSuppression(intensity : ImageData, direction : ImageData) : ImageData
	{
		var temp : Pair<Pair<Int, Int>, Pair<Int, Int>>
		var positionList : List<Pair<Int, Int>>
		val suppressedIntensity = intensity.copy()
		
		direction.forEachXY()
		{
			temp = getDirection((direction[it] as GrayScaleFactory.GrayScale).grayScale).operation(it)
			positionList = listOf(temp.first, it, temp.second)
			if (positionList.map { intensity[it].asGray }.let { (it[0] < it[1]) == (it[1] < it[2]) })
				suppressedIntensity[it] = GrayScaleFactory.GrayScale.Black
		}
		return suppressedIntensity
	}
	private enum class Direction(inline val operation : (Pair<Int, Int>) -> Pair<Pair<Int, Int>, Pair<Int, Int>>)
	{
		N2S({ (x, y) -> (x to y + 1) to (x to y - 1) }),
		E2W({ (x, y) -> (x - 1 to y) to (x + 1 to y) }),
		NE2SW({ (x, y) -> (x + 1 to y + 1) to (x - 1 to y - 1) }),
		NW2SE({ (x, y) -> (x + 1 to y - 1) to (x - 1 to y + 1) })
	}
	private fun getDirection(tanAngle : Double) : Direction = when (tanAngle)
	{
		in boundaries[0]..boundaries[1] -> Direction.NW2SE
		in boundaries[1]..boundaries[2] -> Direction.E2W
		in boundaries[2]..boundaries[3] -> Direction.NE2SW
		else -> Direction.N2S
	}
	private val Double.abs get() = Math.abs(this)
	private fun sortEdges(data : ImageData) = data.map { it.asGray.abs }.sorted()
	private fun doubleThreshold(data : ImageData, sortedEdge : List<Double>, threshold : (List<Double>) -> Pair<Double, Double>)
	{
		val (strong, weak) = threshold(sortedEdge)
		data.forEachIndexed { index, color -> data[index] = when
		{
			color.asGray.abs > strong -> Edge.Strong
			color.asGray.abs < weak -> Edge.None
			else -> Edge.Weak
		}}
	}
	private fun trackEdge(data : ImageData)
	{
		val originalStrongPoints = emptyList<Pair<Int, Int>>().toMutableList()
		data.forEachXY{ it.takeIf { data[it].asEdge == Edge.EdgeType.Strong }?.let { originalStrongPoints.add(it) }}
		originalStrongPoints.forEach{ extendSingleStrongPoint(data, it) }
		data.data.indices.forEach { if(data[it].asEdge == Edge.EdgeType.Weak) data[it] = Edge.None }
	}
	private fun extendSingleStrongPoint(data : ImageData, position : Pair<Int, Int>)
	{
		if (data[position].asEdge != Edge.EdgeType.Strong) return
		val stack = Stack<Pair<Int, Int>>().apply { push(position) }
		var param : Pair<Int, Int>
		while (stack.isNotEmpty())
		{
			param = stack.pop()
			adjacent(param).forEach()
			{
				if (data[it].asEdge == Edge.EdgeType.Weak)
				{
					data[it] = Edge.Strong
					stack.push(it)
				}
			}
		}
	}
}