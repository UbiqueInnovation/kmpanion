package ch.ubique.libs.kmpanion.compose.adaptive.config

import androidx.compose.ui.unit.Dp
import ch.ubique.libs.kmpanion.compose.adaptive.layout.AdaptiveWidthClass

/**
 * Defines the maximum content width for each adaptive width class.
 *
 * An unspecified width allows content to use all available horizontal space.
 * Later width classes inherit the previous class's value unless specified.
 */
data class AdaptivePolicy(
	val compactMaxWidth: Dp = Dp.Unspecified,
	val mediumMaxWidth: Dp = compactMaxWidth,
	val expandedMaxWidth: Dp = mediumMaxWidth,
) {

	fun resolveMaxWidth(widthClass: AdaptiveWidthClass): Dp = when (widthClass) {
		AdaptiveWidthClass.Compact -> compactMaxWidth
		AdaptiveWidthClass.Medium -> mediumMaxWidth
		AdaptiveWidthClass.Expanded -> expandedMaxWidth
	}
}
