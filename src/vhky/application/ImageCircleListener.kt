package vhky.application

import javafx.event.EventHandler
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent

/**
 * No Description
 *
 * Created at 18:08 2017/8/3
 * @author VHKY
 */
class ImageCircleListener(val imageView : ImageView) : EventHandler<MouseEvent>
{
	val writer get() = ((imageView.image) as? WritableImage)?.pixelWriter
	override fun handle(event : MouseEvent)
	{
		adjacent(event.x / imageView.width * imageView.image.width, event.y / imageView.height * imageView.image.height ).forEach { drawRed(it) }
	}
	fun adjacent(x : Double, y : Double) : List<Pair<Double, Double>>
	{
		val seed = 5.let { param -> List(param * 2 + 1, { -param + it }) }.map { it.toDouble() }
		return (0..seed.size * seed.size - 1).map { x + seed[it / seed.size] to y + seed[it % seed.size]}
	}
	private fun drawRed(position : Pair<Double, Double>)
	{
		if (position.first >= 0 && position.second >= 0 && position.first <= imageView.image.width - 1 && position.second <= imageView.image.height - 1)
		writer?.setArgb((position.first).toInt(), (position.second).toInt(), 0xFFFF0000.toInt())
	}
}
val ImageView.width : Double get()
{
	if (getSmallerSide(this)) return this.fitWidth
	else return this.fitHeight / image.height * image.width
}
val ImageView.height : Double get()
{
	if (!getSmallerSide(this)) return this.fitHeight
	else return this.fitWidth / image.width * image.height
}
//true for width, false for height
fun getSmallerSide(view : ImageView) : Boolean
{
	val imageRatio = view.image.height / view.image.width
	val viewRatio = view.fitHeight / view.fitWidth
	return viewRatio > imageRatio
	
}