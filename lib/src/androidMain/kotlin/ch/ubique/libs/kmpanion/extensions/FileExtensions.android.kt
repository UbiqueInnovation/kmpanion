package ch.ubique.libs.kmpanion.extensions

import androidx.exifinterface.media.ExifInterface
import java.io.File

/**
 * Get the image's rotation as specified by the EXIF metadata of the given file.
 * Use [ExifInterface] and
 * add androidx.exifinterface:exifinterface:
 * [version](https://developer.android.com/jetpack/androidx/releases/exifinterface) dependency in Gradle.
 * @see ExifInterface
 * @return Angle in degrees.
 * @throws java.io.IOException
 */
fun File.getImageRotation(): Int {
	val orientation = ExifInterface(absolutePath).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
	return when (orientation) {
		ExifInterface.ORIENTATION_ROTATE_90 -> 90
		ExifInterface.ORIENTATION_ROTATE_180 -> 180
		ExifInterface.ORIENTATION_ROTATE_270 -> 270
		else -> 0
	}
}
