package vhky

/**
 * No Description
 *
 * Created at 15:02 2017/7/27
 * @author VHKY
 */

fun main(args : Array<String>)
{
	(1..3).let { bound -> bound.flatMap { x -> bound.map { y -> x to y } } }.forEach { (x, y) -> println("($x, $y)") }
}
