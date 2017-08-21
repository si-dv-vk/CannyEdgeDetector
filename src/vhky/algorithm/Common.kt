package vhky.algorithm

import vhky.algorithm.data.ImageCursor


/**
 * No Description
 *
 * Created at 19:13 2017/8/8
 * @author VHKY
 */

fun adjacent(position : ImageCursor) : List<Pair<Int, Int>>
{
	val seed = listOf(-1, 0, 1)
	return (0..8).map { position.x + seed[it / 3] to position.y + seed[it % 3] }
}
fun adjacent(position : Pair<Int, Int>) : List<Pair<Int, Int>>
{
	val seed = listOf(-1, 0, 1)
	return (0..8).map { position.first + seed[it / 3] to position.second + seed[it % 3] }
}