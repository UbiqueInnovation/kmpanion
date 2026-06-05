package ch.ubique.libs.kmpanion.compose.screen

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleStartEffect

@Composable
fun ScreenshotProtection() {
	val window = LocalActivity.current?.window
	LifecycleStartEffect(Unit) {
		window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

		onStopOrDispose {
			window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
		}
	}
}