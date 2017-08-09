package vhky.algorithm.data

/**
 * No Description
 *
 * Created at 10:22 2017/8/9
 * @author VHKY
 */
class ImageCursor(val imageSize : ImageSize)
{
	var index = 0
	get() = field
	set(value)
	{
		if (value >= 0 && value < imageSize.size)
			field = value
	}
	operator fun component1() = index % imageSize.width
	operator fun component2() = index / imageSize.width
	var x get() = component1()
	set(value)
	{
		val newX = when
		{
			value < 0 -> 0
			value >= imageSize.width -> imageSize.width - 1
			else -> value
		}
		moveTo(newX, y)
	}
	
	var y get() = component2()
	set(value)
	{
	
	}
	private fun moveTo(x : Int, y : Int)
	{
	
	}
	
}