package ch.ubique.libs.kmpanion.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

private val IDENTITY_MATRIX = Matrix()

/**
 * Overlay the copy of a mutable Bitmap with another foreground Bitmap.
 * @param foreground Bitmap to be drawn over the given background.
 * @param matrix Transformation to be applied to the foreground. Default: identity matrix
 * @param paint Paint used for drawing the foreground onto the background. Default: null
 * @return Combined result in a new Bitmap. Null, if the Bitmap could not be copied.
 */
fun Bitmap.overlaid(foreground: Bitmap, matrix: Matrix = IDENTITY_MATRIX, paint: Paint? = null): Bitmap? =
	config?.let {
		copy(it, true)?.apply {
			overlay(foreground, matrix, paint)
		}
	}

/**
 * Overlay a Bitmap with another foreground Bitmap.
 * @param foreground Bitmap to be drawn over the given background.
 * @param matrix Transformation to be applied to the foreground. Default: identity matrix
 * @param paint Paint used for drawing the foreground onto the background. Default: null
 * @return Combined result in the background Bitmap.
 */
fun Bitmap.overlay(foreground: Bitmap, matrix: Matrix = IDENTITY_MATRIX, paint: Paint? = null) {
	Canvas(this).drawBitmap(foreground, matrix, paint)
}

/**
 * Rotate the given Bitmap by `angle`.
 * @param angle Angle in degrees
 * @return A new Bitmap as the rotated version of the original.
 */
fun Bitmap.rotated(angle: Float): Bitmap {
	val matrix = Matrix()
	matrix.postRotate(angle)
	return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
