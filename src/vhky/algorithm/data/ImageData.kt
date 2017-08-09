package vhky.algorithm.data

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import vhky.algorithm.data.color.Color
import vhky.algorithm.data.color.ColorFactory
import vhky.algorithm.data.color.GrayScaleFactory

/**
 * Class used to deposit separate color channels.
 * Provide support for converting image to my data container.
 * Provide support for synthesizing image with stored image data
 *
 * Created at 18:46 2017/7/31
 * @author VHKY
 */
class ImageData(val width : Int, val height : Int) : Iterable<Color>
{
	companion object
	{
		fun <T : Color> fromImage(image : Image, colorFactory : ColorFactory<T>) : ImageData
		{
			val product = ImageData(image.width.toInt(), image.height.toInt())
			val reader = image.pixelReader
			product.forEachXY { (x, y) -> product[x, y] = colorFactory.produce(reader.getArgb(x, y)) }
			return product
		}
	}
	val data = Array<Color>(width * height, { GrayScaleFactory.GrayScale.Black })
	fun xyToIndex(x : Int, y : Int) : Int
	{
		var _x = x
		var _y = y
		when
		{
			_x < 0 -> _x = 0
			_x >= width -> _x = width - 1
		}
		when
		{
			_y < 0 -> _y = 0
			_y >= height -> _y = height - 1
		}
		return _x + _y * width
	}
	fun indexToXY(index : Int) = index % width to index / width
	
	operator fun get(index : Int) = data[index]
	operator fun get(x : Int, y : Int) = data[xyToIndex(x, y)]
	operator fun get(position : Pair<Int, Int>) = this[position.first, position.second]
	operator fun set(index : Int, value : Color) {data[index] = value}
	operator fun set(position : Pair<Int, Int>, value : Color) {this[position.first, position.second] = value}
	operator fun set(position : Pair<Int, Int>, value : Double) {this[position.first, position.second] = GrayScaleFactory.GrayScale(value)}
	operator fun set(x : Int, y : Int, value : Color) {data[xyToIndex(x, y)] = value}
	
	override fun iterator() = data.iterator()
	inline fun forEachXY(operation : (Pair<Int, Int>) -> Unit) = data.indices.forEach { operation(indexToXY(it)) }
	fun toImage() : Image
	{
		val product = WritableImage(this.width, this.height)
		val writer = product.pixelWriter
		forEachXY { (x, y) -> writer.setArgb(x, y, this[x, y].getARGB()) }
		return product
	}
	fun copy() : ImageData
	{
		val result = ImageData(this.width, this.height)
		this.forEachIndexed { index, color -> result[index] = color }
		return result
	}
}