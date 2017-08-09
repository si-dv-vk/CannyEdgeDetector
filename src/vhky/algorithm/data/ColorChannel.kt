package vhky.algorithm.data

/**
 * No Description
 *
 * Created at 9:48 2017/8/9
 * @author VHKY
 */
data class ColorChannel(private val data : DoubleArray, val size : ImageSize)
{
	init
	{
		require(data.size == size.size)
	}
	override fun equals(other : Any?) : Boolean
	{
		return data == other
	}
	override fun hashCode() = data.hashCode()
	
}