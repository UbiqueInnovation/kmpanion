package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.ui.unit.Dp

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
