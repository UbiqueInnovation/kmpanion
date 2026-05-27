@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

import java.security.MessageDigest
import kotlin.text.toHexString

inline fun ByteArray.md5(): String = md5Bytes().toHexString()

fun ByteArray.md5Bytes(): ByteArray = MessageDigest.getInstance("MD5").digest(this)