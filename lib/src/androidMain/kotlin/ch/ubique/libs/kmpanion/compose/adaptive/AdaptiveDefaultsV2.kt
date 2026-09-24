package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AdaptiveLayoutConfig(
	val compactContentPadding: Dp = 16.dp,
	val mediumContentPadding: Dp = 24.dp,
	val expandedContentPadding: Dp = 48.dp,
	val mediumContentMaxWidth: Dp = 720.dp,
	val expandedContentMaxWidth: Dp = 840.dp,
	val actionMaxWidth: Dp = 420.dp,
	val sheetMaxWidth: Dp = 420.dp,
)

val LocalAdaptiveLayoutConfig = staticCompositionLocalOf {
	AdaptiveLayoutConfig()
}

object AdaptiveDefaults {
	fun contentPadding(info: AdaptiveLayoutInfo, config: AdaptiveLayoutConfig): Dp = when (info.widthClass) {
		AdaptiveWidthClass.Compact -> config.compactContentPadding
		AdaptiveWidthClass.Medium -> config.mediumContentPadding
		AdaptiveWidthClass.Expanded -> config.expandedContentPadding
	}

	fun contentMaxWidth(info: AdaptiveLayoutInfo, config: AdaptiveLayoutConfig): Dp = when (info.widthClass) {
		AdaptiveWidthClass.Compact -> Dp.Unspecified
		AdaptiveWidthClass.Medium -> config.mediumContentMaxWidth
		AdaptiveWidthClass.Expanded -> config.expandedContentMaxWidth
	}
}
