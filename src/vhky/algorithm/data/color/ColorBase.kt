package vhky.algorithm.data.color

/**
 * No Description
 *
 * Created at 10:20 2017/8/1
 * @author VHKY
 */
abstract class Color
{
	abstract fun getARGB() : Int
	override fun equals(other : Any?) : Boolean
	{
		if (other !is Color) return false
		return getARGB() == other.getARGB()
	}
	
	override fun hashCode() = getARGB().hashCode()
}
interface ColorFactory<out T : Color>
{
	fun produce(argb : Int) : T
	companion object
	{
		val DefaultSampler : (Double) -> Int =
				{
					var value = Math.round(it)
					if (value < 0) value = -value
					if (value > 0xFF) 0xFF
					else value.toInt()
				}
		val A : (Int) -> Int = { it ushr 24 and 0xFF }
		val R : (Int) -> Int = { it ushr 16 and 0xFF }
		val G : (Int) -> Int = { it ushr 8 and 0xFF }
		val B : (Int) -> Int = { it and 0xFF }
		val ARGB : (a : Int, r : Int, g : Int, b : Int) -> Int = { a, r, g, b -> (a shl 24) or (r shl 16) or (g shl 8) or b }
	}
}