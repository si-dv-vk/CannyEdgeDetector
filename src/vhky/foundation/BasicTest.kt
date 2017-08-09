package vhky.foundation

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * No Description
 *
 * Created at 11:22 2017/8/1
 * @author VHKY
 */
abstract class BasicTest
{
	@Test
	fun antiCheat()
	{
		try
		{
			assertTrue { false }
		}
		catch (e : AssertionError)
		{
			assertTrue { true }
			return
		}
		throw RuntimeException("Cheat Detected")
	}
}