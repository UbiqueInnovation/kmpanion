package ch.ubique.libs.kmpanion.compose.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LargeBitmapImageDefaults {
	val transitionSpec: AnimatedContentTransitionScope<ImageBitmap?>.() -> ContentTransform = {
		fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90)) togetherWith fadeOut(animationSpec = tween(90))
	}

	val loadingContent: @Composable () -> Unit = {
		Box(contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}
	}
}

@Composable
fun LargeBitmapImage(
	imageBytes: State<ByteArray?>,
	modifier: Modifier = Modifier,
	longSideMaxLength: Int = 1080,
	transitionSpec: AnimatedContentTransitionScope<ImageBitmap?>.() -> ContentTransform = LargeBitmapImageDefaults.transitionSpec,
	loadingContent: @Composable () -> Unit = LargeBitmapImageDefaults.loadingContent,
	imageContent: @Composable (image: ImageBitmap) -> Unit,
) {
	val bitmapState = rememberImageBitmap(imageBytes.value, longSideMaxLength)

	LargeBitmapImage(
		bitmapState = bitmapState,
		modifier = modifier,
		transitionSpec = transitionSpec,
		loadingContent = loadingContent,
		imageContent = imageContent,
	)
}

@Composable
fun LargeBitmapImage(
	imageBytes: ByteArray,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	contentScale: ContentScale = ContentScale.Fit,
	longSideMaxLength: Int = 1080,
	transitionSpec: AnimatedContentTransitionScope<ImageBitmap?>.() -> ContentTransform = LargeBitmapImageDefaults.transitionSpec,
	loadingContent: @Composable () -> Unit = LargeBitmapImageDefaults.loadingContent,
) {
	val bitmapState = rememberImageBitmap(imageBytes, longSideMaxLength)

	LargeBitmapImage(
		bitmapState = bitmapState,
		modifier = modifier,
		transitionSpec = transitionSpec,
		loadingContent = loadingContent,
		imageContent = { bitmap ->
			Image(
				bitmap = bitmap,
				contentDescription = contentDescription,
				contentScale = contentScale,
			)
		},
	)
}

@Composable
private fun LargeBitmapImage(
	bitmapState: State<ImageBitmap?>,
	modifier: Modifier = Modifier,
	transitionSpec: AnimatedContentTransitionScope<ImageBitmap?>.() -> ContentTransform,
	loadingContent: @Composable () -> Unit,
	imageContent: @Composable (image: ImageBitmap) -> Unit,
) {
	AnimatedContent(
		modifier = modifier,
		targetState = bitmapState.value,
		transitionSpec = transitionSpec,
	) { bitmap ->
		if (bitmap != null) {
			imageContent(bitmap)
		} else {
			loadingContent()
		}
	}
}

@Composable
private fun rememberImageBitmap(imageBytes: ByteArray?, longSideMaxLength: Int): State<ImageBitmap?> {
	return if (LocalInspectionMode.current) {
		// Decode synchronously for the inspection preview
		remember { mutableStateOf(imageBytes?.decodeToImageBitmap()) }
	} else {
		// Decode asynchronously in normal mode
		produceState(initialValue = null, key1 = imageBytes) {
			if (imageBytes != null) {
				withContext(Dispatchers.Default) {
					val scaledBitmap = loadBitmapWithLongSideAtMost(imageBytes, longSideMaxLength)
					value = scaledBitmap.asImageBitmap()
				}
			}
		}
	}
}

private fun loadBitmapWithLongSideAtMost(imageBytes: ByteArray, maxLength: Int = 1080): Bitmap {
	// First decode with inJustDecodeBounds=true to check dimensions
	val options = BitmapFactory.Options()
	options.inJustDecodeBounds = true
	BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
	val originalHeight = options.outHeight
	val originalWidth = options.outWidth
	val aspectRatio = originalWidth / originalHeight.toFloat()

	// Calculate the target width and height based on the maxLength while maintaining the aspect ratio
	val (reqWidth, reqHeight) = if (aspectRatio > 1f) {
		// Landscape orientation
		val newWidth = originalWidth.coerceAtMost(maxLength)
		val newHeight = (originalHeight * (newWidth / originalWidth.toFloat())).toInt()
		newWidth to newHeight
	} else {
		// Portrait orientation
		val newHeight = originalHeight.coerceAtMost(maxLength)
		val newWidth = (originalWidth * (newHeight / originalHeight.toFloat())).toInt()
		newWidth to newHeight
	}

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false
	val sampledBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
	val resizedBitmap = sampledBitmap.scale(reqWidth, reqHeight, false)
	if (sampledBitmap != resizedBitmap) {
		sampledBitmap.recycle()
	}
	return resizedBitmap
}

/**
 * @see <a href="https://developer.android.com/topic/performance/graphics/load-bitmap#load-bitmap">Android Developers Guide</a>
 */
private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
	// Raw height and width of image
	val height = options.outHeight
	val width = options.outWidth
	var inSampleSize = 1
	if (height > reqHeight || width > reqWidth) {
		val halfHeight = height / 2
		val halfWidth = width / 2

		// Calculate the largest inSampleSize value that is a power of 2 and keeps both
		// height and width larger than the requested height and width.
		while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
			inSampleSize *= 2
		}
	}
	return inSampleSize
}