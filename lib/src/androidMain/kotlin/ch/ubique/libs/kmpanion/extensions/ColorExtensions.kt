package ch.ubique.libs.kmpanion.extensions

import androidx.annotation.FloatRange
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

fun Color.alpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = this.copy(alpha = alpha)

@Composable
@ReadOnlyComposable
infix fun Color.or(darkModeColor: Color) = if (isSystemInDarkTheme()) darkModeColor else this