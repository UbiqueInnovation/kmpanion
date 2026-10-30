package ch.ubique.libs.kmpanion.compose.adaptive.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Displays the content matching the current adaptive width class.
 *
 * [medium] falls back to [compact], and [expanded] falls back to [medium].
 */
@Composable
fun AdaptiveLayout(
	compact: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	medium: @Composable () -> Unit = compact,
	expanded: @Composable () -> Unit = medium,
) {
	Box(modifier = modifier) {
		when (info.widthClass) {
			AdaptiveWidthClass.Compact -> compact()
			AdaptiveWidthClass.Medium -> medium()
			AdaptiveWidthClass.Expanded -> expanded()
		}
	}
}
