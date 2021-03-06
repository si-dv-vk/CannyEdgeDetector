package vhky.algorithm.data

/**
 * Image data container
 *
 * Created at 9:48 2017/8/9
 * @author VHKY
 */
data class ImageData(private val data : DoubleArray, val size : ImageSize) : Iterable<Double>
{
	init
	{
		require(data.size == size.size)
	}
	constructor(size : ImageSize) : this(DoubleArray(size.size), size)
	constructor(width : Int, height : Int) : this(ImageSize(width, height))
	override fun equals(other : Any?) : Boolean
	{
		return data == other
	}
	override fun hashCode() = data.hashCode()
	operator fun get(cursor : ImageCursor) = data[cursor.index]
	operator fun get(index : Int) = data[index]
	operator fun set(index : Int, value : Double) = data.set(index, value)
	operator fun get(position : Pair<Int, Int>) = this[position.first, position.second]
	operator fun set(position : Pair<Int, Int>, value : Double) = this.set(position.first, position.second, value)
	operator fun set(cursor : ImageCursor, value : Double) = data.set(cursor.index, value)
	operator fun get(x : Int, y : Int) = this[_cursor.apply { set(x, y) }]
	operator fun set(x : Int, y : Int, value : Double) = this.set(_cursor.apply { set(x, y) }, value)
	override fun iterator() = data.iterator()
	private val _cursor = ImageCursor(size)
	fun copy() = ImageData(data.copyOf(), size)
	fun set(data : ImageData) = data.forEachIndexed { index, _data -> this.data[index] = _data }
	fun forEachXY(action : (ImageCursor) -> Unit) = size.forEach(action)
	val width by lazy { size.width }
	val height by lazy { size.height }
}