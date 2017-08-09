package vhky.algorithm.data.color

/**
 * No Description
 *
 * Created at 17:48 2017/8/2
 * @author VHKY
 */
object SimpleRGBColorFactory : ColorFactory<SimpleRGBColorFactory.SimpleRGBColor>
{
	override fun produce(argb : Int) = SimpleRGBColor(argb)
	data class SimpleRGBColor(val r : Double, val g : Double, val b : Double) : Color()
	{
		constructor(r : Int, g : Int, b : Int) : this(r.toDouble(), g.toDouble(), b.toDouble())
		constructor(argb : Int) : this(ColorFactory.R(argb), ColorFactory.G(argb), ColorFactory.B(argb))
		enum class ColorChanel{R, G, B}
		override fun getARGB() = ColorFactory.ARGB(0xFF, ColorFactory.DefaultSampler(r), ColorFactory.DefaultSampler(g), ColorFactory.DefaultSampler(b))
		operator fun get(chanel : ColorChanel) = when(chanel)
		{
			ColorChanel.R -> r
			ColorChanel.G -> g
			ColorChanel.B -> b
		}
	}
}