package vhky.test

import org.junit.jupiter.api.Test
import vhky.algorithm.data.ImageData
import vhky.algorithm.data.color.GrayScaleFactory
import vhky.foundation.BasicTest
import java.util.*
import kotlin.test.assertEquals

/**
 * No Description
 *
 *
 * Created at 10:46 2017/8/3

 * @author VHKY
 */
internal class ImageDataTest : BasicTest()
{
	val random = Random(System.currentTimeMillis())
	@Test
	fun edgeTest()
	{
		val data = ImageData(3, 3)
		data.forEachIndexed { index, _ -> data[index] =  GrayScaleFactory.GrayScale(random.nextInt(255).toDouble())}
		assertEquals(data[0, 0], data[-1, -1])
		assertEquals(data[0, 2], data[-2, 2])
	}
}