package vhky.algorithm.data.color

/**
 * No Description
 *
 * Created at 18:00 2017/8/2
 * @author VHKY
 */
object GrayScaleFactory : ColorFactory<GrayScaleFactory.GrayScale>
{
	override fun produce(argb : Int) = GrayScale(argb)
	data class GrayScale(val grayScale : Double) : Color()
	{
		companion object
		{
			private val RFactor = 0.2126
			private val GFactor = 0.7152
			private val BFactor = 0.0722
			val White = GrayScale(255.0)
			val Black = GrayScale(0.0)
		}
		constructor(argb : Int) : this(RFactor * ColorFactory.R(argb) +
		GFactor * ColorFactory.G(argb) + BFactor * ColorFactory.B(argb))
		
		val sampled by lazy { ColorFactory.DefaultSampler(grayScale) }
		override fun getARGB() = ColorFactory.ARGB(0xFF, sampled, sampled, sampled)
	}
}
val Color.asGray get() = (this as GrayScaleFactory.GrayScale).grayScale