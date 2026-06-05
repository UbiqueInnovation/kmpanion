package ch.ubique.libs.kmpanion.math

import kotlin.math.sqrt

fun euclideanDistance(aX: Float, aY: Float, bX: Float, bY: Float): Float {
	val d1 = bX - aX
	val d2 = bY - aY
	return sqrt(d1 * d1 + d2 * d2)
}