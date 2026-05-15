@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

fun String.longHashCode(): Long {
	var h = 0L
	for (element in this) {
		h = 31 * h + element.code
	}
	return h
}

/**
 * Get the enum value matching this String.
 * If no match was found and no fallback was specified, an IllegalArgumentException is thrown.
 * @param ignoreCase true to match case-insensitive, defaults to false.
 * @param fallback enum value to use in case of an unknown value.
 */
inline fun <reified E : Enum<E>> String?.toEnum(ignoreCase: Boolean = false, fallback: E? = null): E {
	return enumValues<E>().firstOrNull { it.name.equals(this, ignoreCase) }
		?: requireNotNull(fallback) { "No match found for '$this' in " + E::class.simpleName + ", no fallback provided" }
}

/**
 * Get the enum value matching this String.
 * @param ignoreCase true to match case-insensitive, defaults to false.
 * @param fallback enum value to use in case of an unknown value, defaults to null.
 */
inline fun <reified E : Enum<E>> String?.toEnumNullable(ignoreCase: Boolean = false, fallback: E? = null): E? {
	return enumValues<E>().firstOrNull { it.name.equals(this, ignoreCase) } ?: fallback
}
