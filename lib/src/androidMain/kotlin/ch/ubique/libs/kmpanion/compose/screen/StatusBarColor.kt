package ch.ubique.libs.kmpanion.compose.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LifecycleResumeEffect

/**
 * Changes the statusbar color to use light or dark appearance
 */
@Composable
fun StatusBarColor(
	useLightStatusBarIcons: Boolean = isSystemInDarkTheme(),
) {
	val view = LocalView.current
	if (!view.isInEditMode) {
		val window = LocalActivity.current?.window ?: return
		val insetsController = WindowCompat.getInsetsController(window, view)

		LaunchedEffect(Unit) {
			insetsController.isAppearanceLightStatusBars = !useLightStatusBarIcons
		}

		LifecycleResumeEffect(Unit) {
			insetsController.isAppearanceLightStatusBars = !useLightStatusBarIcons
			onPauseOrDispose {}
		}
	}
}