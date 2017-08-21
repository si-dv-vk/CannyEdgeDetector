package vhky.algorithm.edge

import javafx.scene.image.Image
import vhky.algorithm.adjacent
import vhky.algorithm.convolution.ConvolutionKernel
import vhky.algorithm.convolution.timesAssign
import vhky.algorithm.data.GrayScaleFactory
import vhky.algorithm.data.ImageCursor
import vhky.algorithm.data.ImageData
import vhky.algorithm.despeckle.EdgePreservingFilter
import vhky.algorithm.despeckle.gaussianKernel
import java.util.*

/**
 * The main algorithm pipeline
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
		val data = GrayScaleFactory.fromImage(image)
		return GrayScaleFactory.toImage(pipeline(data))
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
		val _data = DespeckleFilter(data)
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
	private fun DespeckleFilter(data : ImageData) : ImageData = EdgePreservingFilter.process(data, 3, 3.0)
	private val sqrt2 = Math.sqrt(2.0)
	private val SobelX = ConvolutionKernel(listOf(
			-1.0, 0.0, 1.0,
			-sqrt2, 0.0, sqrt2,
			-1.0, 0.0, 1.0
	).map { it / (2 + sqrt2) })
	private val SobelY = ConvolutionKernel(listOf(
			1.0, sqrt2, 1.0,
			0.0, 0.0, 0.0,
			-1.0, -sqrt2, -1.0
	).map { it / (2 + sqrt2) })
	private fun getIntensityGradients(data : ImageData) : Pair<ImageData, ImageData>
	{
		println("Sobel starts")
		val startTime = System.currentTimeMillis()
		val sx = data.copy()
		val sy = data.copy()
		val intensity = ImageData(data.width, data.height)
		val direction = ImageData(data.width, data.height)
		sx *= SobelX
		sy *= SobelY
		intensity.size.forEach { intensity[it] = Math.hypot(sy[it], sx[it]) }
		direction.size.forEach { direction[it] = Math.atan2(sy[it], sx[it]) }
		println("Sobel finishes, time = ${System.currentTimeMillis() - startTime}ms")
		return intensity to direction
	}
	private val boundaries = List(4, { Math.PI * (-3.0 / 8.0 + 0.25 * it)})
	private fun nonMaximumSuppression(intensity : ImageData, direction : ImageData) : ImageData
	{
		val startTime = System.currentTimeMillis()
		println("Non-maximum suppression starts")
		var temp : Pair<Pair<Int, Int>, Pair<Int, Int>>
		var positionList : List<Pair<Int, Int>>
		val suppressedIntensity = intensity.copy()
		
		direction.forEachXY()
		{
			temp = getDirection(direction[it]).operation(it)
			positionList = listOf(temp.first, it.x to it.y, temp.second)
			if (positionList.map { intensity[it] }.let { (it[0] < it[1]) == (it[1] < it[2]) })
				suppressedIntensity[it] = 0.0
		}
		
		println("Non-Maximum suppression finishes, time = ${System.currentTimeMillis() - startTime}ms")
		return suppressedIntensity
	}
	private enum class Direction(inline val operation : (ImageCursor) -> Pair<Pair<Int, Int>, Pair<Int, Int>>)
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
	private fun sortEdges(data : ImageData) = data.map { it.abs }.sorted()
	private fun doubleThreshold(data : ImageData, sortedEdge : List<Double>, threshold : (List<Double>) -> Pair<Double, Double>)
	{
		val (strong, weak) = threshold(sortedEdge)
		data.forEachIndexed { index, color -> data[index] = when
		{
			color.abs > strong -> 255.0
			color.abs < weak -> 0.0
			else -> 128.0
		}}
	}
	private fun trackEdge(data : ImageData)
	{
		val originalStrongPoints = emptyList<Pair<Int, Int>>().toMutableList()
		data.forEachXY{ it.takeIf { data[it] == 255.0 }?.let { originalStrongPoints.add(it.x to it.y) }}
		originalStrongPoints.forEach{ extendSingleStrongPoint(data, it) }
		data.size.forEach { if(data[it] == 128.0) data[it] = 0.0 }
	}
	private fun extendSingleStrongPoint(data : ImageData, position : Pair<Int, Int>)
	{
		if (data[position] != 255.0) return
		val stack = Stack<Pair<Int, Int>>().apply { push(position) }
		var param : Pair<Int, Int>
		while (stack.isNotEmpty())
		{
			param = stack.pop()
			adjacent(param).forEach()
			{
				if (data[it] == 128.0)
				{
					data[it] = 255.0
					stack.push(it)
				}
			}
		}
	}
}