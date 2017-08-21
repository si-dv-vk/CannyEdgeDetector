package vhky.algorithm.data

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

/**
 * No Description
 *
 * Created at 16:37 2017/8/21
 * @author VHKY
 */
object GrayScaleFactory
{
	private val Int.gray get() = (this ushr 16 and 0xFF) * 0.2126 + (this ushr 8 and 0xFF) * 0.7152 + (this and 0xFF) * 0.0722
	private val Double.argb get() = Math.round(this).toInt().let { gray -> (0..2).map { gray shl it * 0x8 }.sum() or (0xFF shl 0x18) }.toInt()
	fun fromImage(image : Image) = ImageData(image.width.toInt(), image.height.toInt()).apply()
	{
		val startTime = System.currentTimeMillis()
		println("Gray-Scaling starts")
		forEachXY { (x, y) -> image.pixelReader.getArgb(x, y).gray.let { this[x, y] = it } }
		println("Gray-Scaling finishes, time = ${System.currentTimeMillis() - startTime}ms")
	}
	fun toImage(data : ImageData) : Image = WritableImage(data.width, data.height).apply()
	{
		data.forEachXY { (x, y) -> pixelWriter.setArgb(x, y, data[x, y].argb) }
	}
}