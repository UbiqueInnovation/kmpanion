package ch.ubique.libs.kmpanion.extensions

import android.graphics.PointF
import ch.ubique.libs.kmpanion.math.euclideanDistance

fun PointF.euclideanDistanceTo(other: PointF): Float {
	return euclideanDistance(x, y, other.x, other.y)
}

fun PointF.euclideanDistanceTo(otherX: Float, otherY: Float): Float {
	return euclideanDistance(x, y, otherX, otherY)
}