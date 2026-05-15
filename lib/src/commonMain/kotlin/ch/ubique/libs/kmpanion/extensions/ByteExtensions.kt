package ch.ubique.libs.kmpanion.extensions

private val HEX_ARRAY: CharArray = "0123456789abcdef".toCharArray()

fun Byte.toHexString(): String {
	val v: Int = toInt() and 0xFF
	return "${HEX_ARRAY[v shr 4]}${HEX_ARRAY[v and 0x0F]}"
}
