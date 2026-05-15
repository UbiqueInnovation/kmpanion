@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

@Suppress("UNCHECKED_CAST")
inline fun <K, V> Map<K, V?>.filterNotNullValues() = filterValues { it != null } as Map<K, V>
