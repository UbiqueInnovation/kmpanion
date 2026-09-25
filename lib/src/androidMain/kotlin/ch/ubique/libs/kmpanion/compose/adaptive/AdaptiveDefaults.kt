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
	// Buttons and other action objects
	val actionMaxWidth: Dp = 420.dp,
	// Sheets and dialogs
	val sheetMaxWidth: Dp = 420.dp,
	val singlePaneMaxWidth: Dp = 640.dp,
)

object ContentRole : AdaptiveRole
object ActionRole : AdaptiveRole
object SheetRole : AdaptiveRole

val DefaultAdaptiveRoleConfig = AdaptiveRoleConfig(
	mapOf(
		ContentRole to AdaptivePolicy(
			mediumMaxWidth = 720.dp,
			expandedMaxWidth = 840.dp,
		),
		ActionRole to AdaptivePolicy(mediumMaxWidth = 420.dp),
		SheetRole to AdaptivePolicy(mediumMaxWidth = 420.dp),
	),
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
