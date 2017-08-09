package vhky.algorithm.data.color

/**
 * No Description
 *
 * Created at 15:36 2017/8/3
 * @author VHKY
 */
class Edge private constructor(val type : EdgeType) : Color()
{
	companion object
	{
		val Weak = Edge(EdgeType.Weak)
		val Strong = Edge(EdgeType.Strong)
		val None = Edge(EdgeType.None)
	}
	enum class EdgeType { None, Weak, Strong }
	override fun getARGB() = when (type)
	{
		EdgeType.None -> 0xFF000000.toInt()
		EdgeType.Weak -> 0xFF808080.toInt()
		EdgeType.Strong -> 0xFFFFFFFF.toInt()
	}
}
val Color.asEdge get() = (this as Edge).type