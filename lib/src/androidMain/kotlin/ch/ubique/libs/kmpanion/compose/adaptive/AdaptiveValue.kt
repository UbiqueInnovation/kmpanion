package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.runtime.Composable
import ch.ubique.libs.kmpanion.compose.adaptive.layout.AdaptiveLayoutInfo
import ch.ubique.libs.kmpanion.compose.adaptive.layout.AdaptiveWidthClass
import ch.ubique.libs.kmpanion.compose.adaptive.layout.rememberAdaptiveLayoutInfo


/**
 * Returns the value matching the current adaptive width class.
 *
 * [medium] falls back to [compact], and [expanded] falls back to [medium].
 */
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
