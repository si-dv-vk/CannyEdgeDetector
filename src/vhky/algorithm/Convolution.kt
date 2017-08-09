package vhky.algorithm

import vhky.algorithm.data.ImageData
import vhky.algorithm.data.color.GrayScaleFactory


/**
 * No Description
 *
 * Created at 19:01 2017/8/2
 * @author VHKY
 */
data class ConvolutionKernel(val data : List<Double>)
{
	val size : Int = Math.sqrt(data.size.toDouble()).toInt()
	
	init
	{
		require(size * size == data.size)
		require(size % 2 == 1)
	}
	val center by lazy { size / 2 }
	operator fun get(x : Int, y : Int) = data[x + y * size]
}

fun convolvePoint(imageData : ImageData, kernel : ConvolutionKernel, x : Int, y : Int) : Double
{
	var sum = 0.0
	for (kx in (0..kernel.size - 1))
	{
		for (ky in 0..kernel.size - 1)
		{
			sum += (imageData[x - kernel.center + kx, y - kernel.center + ky]
					as GrayScaleFactory.GrayScale).grayScale * kernel[kx, ky]
		}
	}
	return sum
}

fun convolve(imageData : ImageData, kernel : ConvolutionKernel)
{
	val backup = ImageData(imageData.width, imageData.height)
	imageData.forEachIndexed { index, color -> backup[index] = color }
	imageData.forEachXY { (x, y) -> imageData[x, y] = GrayScaleFactory.GrayScale(convolvePoint(backup, kernel, x, y)) }
}
operator fun ImageData.timesAssign(kernel : ConvolutionKernel) = convolve(this, kernel)