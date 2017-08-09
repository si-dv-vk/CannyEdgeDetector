package vhky.algorithm

/**
 * No Description
 *
 * Created at 19:13 2017/8/8
 * @author VHKY
 */

fun adjacent(position : Pair<Int, Int>) : List<Pair<Int, Int>>
{
	val seed = listOf(-1, 0, 1)
	return (0..8).map { position.first + seed[it / 3] to position.second + seed[it % 3] }
}