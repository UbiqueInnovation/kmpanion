@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

inline infix fun Boolean.implies(expr: () -> Boolean) = this && expr() || !this

inline fun Boolean.toInt() = if (this) 1 else 0
