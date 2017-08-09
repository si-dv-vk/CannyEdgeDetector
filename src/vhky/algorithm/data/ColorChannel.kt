package vhky.algorithm.data

/**
 * Image data container
 *
 * Created at 9:48 2017/8/9
 * @author VHKY
 */
data class ColorChannel(private val data : DoubleArray, val size : ImageSize) : Iterable<Double>
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
	operator fun get(cursor : ImageCursor) = data[cursor.index]
	operator fun set(cursor : ImageCursor, value : Double) = data.set(cursor.index, value)
	override fun iterator() = data.iterator()
}