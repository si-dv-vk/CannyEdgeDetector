package vhky.algorithm.data

import javafx.scene.image.Image

/**
 * Describing the image size, provide iteration support
 *
 * Created at 9:53 2017/8/9
 * @author VHKY
 */
data class ImageSize(val width : Int, val height : Int) : Iterable<ImageCursor>
{
	companion object
	{
		val Image.size get() = ImageSize(width.toInt(), height.toInt())
	}
	init
	{
		require(width > 0)
		require(height > 0)
	}
	val size = width * height
	override fun iterator() = object : Iterator<ImageCursor>
	{
		val cursor = ImageCursor(this@ImageSize)
		private var index = 0
		override fun hasNext() = index < cursor.imageSize.size
		override fun next() = cursor.apply { cursor.index = index++ }
	}
}