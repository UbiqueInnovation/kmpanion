@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.util.Locale
import kotlin.text.toHexString

/**
 * Converts the first character of this String to upper case using Unicode mapping rules of the specified [locale].
 * This function supports one-to-many character mapping, thus the length of the returned string can be different.
 */
fun String.capitalize(locale: Locale = Locale.ROOT): String {
	return replaceFirstChar { it.uppercase(locale) }
}

/**
 * Replace all accents (äàáâãåā -> aaaaaaa, éèêëė -> eeeee, ...)
 */
fun String.unaccent(): String {
	val regexUnaccent = "\\p{InCombiningDiacriticalMarks}+".toRegex()
	val normalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
	return regexUnaccent.replace(normalizedString, "")
}

fun String.urlEncode(): String {
	return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
		.replace("+", "%20")
}

inline fun String.md5(): String = md5Bytes().toHexString()

inline fun String.md5Bytes(): ByteArray = toByteArray().md5Bytes()
