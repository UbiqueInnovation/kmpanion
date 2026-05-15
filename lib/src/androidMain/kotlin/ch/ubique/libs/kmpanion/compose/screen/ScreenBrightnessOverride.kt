package ch.ubique.libs.kmpanion.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import ch.ubique.libs.kmpanion.extensions.setBrightness

@Composable
fun ScreenBrightnessOverride() {
	val context = LocalContext.current
	DisposableEffect(Unit) {
		context.setBrightness(full = true)
		onDispose {
			context.setBrightness(full = false)
		}
	}
}