package ch.ubique.libs.kmpanion.compose.image

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.vanniktech.blurhash.BlurHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberBlurHashDrawable(blurhash: String, blurhashHeight: Dp, blurhashAspectRatio: Double): State<BitmapDrawable?> {
	val density = LocalDensity.current
	val resources = LocalResources.current

	return if (LocalInspectionMode.current) {
		// Decode synchronously for the inspection preview
		val blurhashBitmap = decodeBlurHashBitmap(density, blurhash, blurhashHeight, blurhashAspectRatio)
		remember { mutableStateOf(BitmapDrawable(resources, blurhashBitmap)) }
	} else {
		// Decode asynchronously in normal mode
		val initial: BitmapDrawable? = null
		produceState(initialValue = initial) {
			// Produce the bitmap drawable state on the IO dispatcher because decoding can take quite a few milliseconds
			withContext(Dispatchers.IO) {
				val blurhashBitmap = decodeBlurHashBitmap(density, blurhash, blurhashHeight, blurhashAspectRatio)
				value = BitmapDrawable(resources, blurhashBitmap)
			}
		}
	}
}

private fun decodeBlurHashBitmap(density: Density, blurhash: String, blurhashHeight: Dp, blurhashAspectRatio: Double): Bitmap? {
	val blurhashWidth = blurhashHeight * blurhashAspectRatio.toFloat()
	val placeholderWidth = with(density) { blurhashWidth.roundToPx() }
	val placeholderHeight = with(density) { blurhashHeight.roundToPx() }
	return BlurHash.decode(blurhash, placeholderWidth, placeholderHeight)
}