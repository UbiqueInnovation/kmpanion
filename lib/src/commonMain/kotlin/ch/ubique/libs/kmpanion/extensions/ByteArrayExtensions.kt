package ch.ubique.libs.kmpanion.extensions

fun ByteArray.toHexString(): String = joinToString("") { it.toHexString() }
