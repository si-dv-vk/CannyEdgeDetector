package vhky.algorithm.data

/**
 * Reusable image pixel pointer
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
	set(value) = set(borderConstraint(value, Which.X), y)
	var y get() = component2()
	set(value) = set(x, borderConstraint(y, Which.Y))
	
	private enum class Which { X, Y }
	
	/**
	 * Constrain x or y inside the border
	 * @param pos value of x or y
	 * @param which the value is x or y
	 * @return the constrained value
	 */
	private fun borderConstraint(pos : Int, which : Which) : Int
	{
		return when
		{
			pos < 0 -> 0
			which == Which.X && pos >= imageSize.width -> imageSize.width - 1
			which == Which.Y && pos >= imageSize.height -> imageSize.height - 1
			else -> pos
		}
	}
	
	/**
	 * Move the cursor to new position.
	 * @param x new x
	 * @param y new y
	 */
	fun set(x : Int, y : Int)
	{
		index = borderConstraint(x, Which.X) + borderConstraint(y, Which.Y) * imageSize.width
	}
}