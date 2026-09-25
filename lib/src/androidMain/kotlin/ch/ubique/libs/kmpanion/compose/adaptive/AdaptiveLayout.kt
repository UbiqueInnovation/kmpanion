package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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

@Composable
fun <T> adaptiveValue(
	compact: T,
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	medium: T = compact,
	expanded: T = medium,
): T = when (info.widthClass) {
	AdaptiveWidthClass.Compact -> compact
	AdaptiveWidthClass.Medium -> medium
	AdaptiveWidthClass.Expanded -> expanded
}
