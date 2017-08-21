package vhky.algorithm.convolution

import vhky.algorithm.data.ImageCursor
import vhky.algorithm.data.ImageData


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
	val indices by lazy { (0 until size).let { bound -> bound.flatMap { x -> bound.map { y -> x to y } } } }
}

fun convolvePoint(data : ImageData, cursor : ImageCursor, kernel : ConvolutionKernel) = kernel.indices
		.map { (kx, ky) -> data[cursor.x - kernel.center + kx, cursor.y - kernel.center + ky] * kernel[kx, ky] }
		.sum()

fun convolve(data : ImageData, kernel : ConvolutionKernel) = ImageData(data.size).apply { data.size.forEach { this[it] = convolvePoint(data, it, kernel) } }
operator fun ImageData.times(kernel : ConvolutionKernel) = convolve(this, kernel)
operator fun ImageData.timesAssign(kernel : ConvolutionKernel) = this.set(this * kernel)