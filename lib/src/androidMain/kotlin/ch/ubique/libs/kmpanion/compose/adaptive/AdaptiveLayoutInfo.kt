package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.window.core.layout.WindowSizeClass

@Immutable
data class AdaptiveLayoutInfo(
	val widthClass: AdaptiveWidthClass,
) {
	val isCompact: Boolean
		get() = widthClass == AdaptiveWidthClass.Compact

	val isMedium: Boolean
		get() = widthClass == AdaptiveWidthClass.Medium

	val isExpanded: Boolean
		get() = widthClass == AdaptiveWidthClass.Expanded

	val isAtLeastMedium: Boolean
		get() = widthClass >= AdaptiveWidthClass.Medium

	val isAtLeastExpanded: Boolean
		get() = widthClass >= AdaptiveWidthClass.Expanded
}

/**
 * Defines different width classes in use
 */
enum class AdaptiveWidthClass {
	Compact,
	Medium,
	Expanded,
}

@Composable
fun rememberAdaptiveLayoutInfo(): AdaptiveLayoutInfo {
	val widthClass = currentWindowAdaptiveInfo().windowSizeClass.toAdaptiveWidthClass()
	return AdaptiveLayoutInfo(widthClass = widthClass)
}

private fun WindowSizeClass.toAdaptiveWidthClass(): AdaptiveWidthClass = when {
	this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> AdaptiveWidthClass.Expanded
	this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> AdaptiveWidthClass.Medium
	else -> AdaptiveWidthClass.Compact
}
