package vhky.algorithm

/**
 * No Description
 *
 * Created at 16:24 2017/8/8
 * @author VHKY
 */


fun gaussianKernel(size : Int) : ConvolutionKernel
{
	require(size % 2 == 1 && size > 1)
	val sigma = size.toDouble() / 3.0
	val center = size / 2
	operator fun DoubleArray.set(x : Int, y : Int, value : Double) = this.set(y * size + x, value)
	
	val dataArray = DoubleArray(size * size)
	(0..size - 1).let { bound -> bound.forEach { x -> bound.forEach { y ->
		dataArray[x, y] = 1.0 / (2.0 * Math.PI * sigma * sigma) * Math.exp(-(Math.pow((x - center).toDouble(), 2.0)
				+ Math.pow((y - center).toDouble(), 2.0)) / (2 * sigma * sigma))
	} } }
	return ConvolutionKernel(dataArray.toList())
}