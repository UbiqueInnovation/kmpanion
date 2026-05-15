@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

inline fun <T> Collection<T>.toArrayList(): ArrayList<T> {
	return this as? ArrayList ?: ArrayList(this)
}

/**
 * Check if this collection contains at least one of the [elements].
 */
inline fun <T> Collection<T>.containsAny(elements: Collection<T>): Boolean = elements.any { contains(it) }
