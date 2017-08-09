package vhky.test.algorithm.data

import org.junit.jupiter.api.Test
import vhky.algorithm.data.ImageSize
import vhky.foundation.BasicTest
import kotlin.test.assertFails

/**
 * No Description
 *
 *
 * Created at 18:36 2017/8/9

 * @author VHKY
 */
internal class ImageSizeTest : BasicTest()
{
	@Test fun invalidSizeTest()
	{
		val testcase = emptyList<Pair<Int, Int>>().toMutableList()
		testcase.addAll((-2..0).let { bound -> bound.flatMap { x -> bound.map { y -> x to y } } })
		testcase.addAll((-2..0).let { it.flatMap { x -> (1..10).map { y -> x to y } } })
		testcase.addAll((1..10).let { it.flatMap { x -> (-2..0).map { y -> x to y } } })
		testcase.forEach { (x, y) -> assertFails { ImageSize(x, y) } }
	}
}