package ch.ubique.libs.kmpanion.compose.extensions

import androidx.compose.runtime.Composable

/**
 * Custom let to allow turning a nullable value into an optional composable lambda
 */
inline fun <T> T?.letComposable(crossinline block: @Composable (T) -> Unit): (@Composable () -> Unit)? {
	return if (this != null) {
		@Composable {
			block.invoke(this)
		}
	} else null
}