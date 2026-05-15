package ch.ubique.libs.kmpanion.extensions

import android.graphics.Point
import ch.ubique.libs.kmpanion.math.euclideanDistance

fun Point.euclideanDistanceTo(other: Point): Float {
	return euclideanDistance(x.toFloat(), y.toFloat(), other.x.toFloat(), other.y.toFloat())
}

fun Point.euclideanDistanceTo(otherX: Int, otherY: Int): Float {
	return euclideanDistance(x.toFloat(), y.toFloat(), otherX.toFloat(), otherY.toFloat())
}