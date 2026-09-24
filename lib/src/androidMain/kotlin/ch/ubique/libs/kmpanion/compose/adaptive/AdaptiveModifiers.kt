package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun Modifier.adaptiveMaxWidth(maxWidth: Dp): Modifier {
	return if (maxWidth == Dp.Unspecified) {
		fillMaxWidth()
	} else {
		requiredWidthIn(max = maxWidth).fillMaxWidth()
	}
}

@Composable
fun Modifier.adaptiveContentWidth(info: AdaptiveLayoutInfo): Modifier {
	return adaptiveContentWidth(info = info, config = LocalAdaptiveLayoutConfig.current)
}

fun Modifier.adaptiveContentWidth(
	info: AdaptiveLayoutInfo,
	config: AdaptiveLayoutConfig,
): Modifier {
	return adaptiveMaxWidth(AdaptiveDefaults.contentMaxWidth(info, config))
}

fun Modifier.adaptiveActionWidth(
	info: AdaptiveLayoutInfo,
	maxWidth: Dp,
): Modifier {
	return if (info.isCompact) {
		fillMaxWidth()
	} else {
		adaptiveMaxWidth(maxWidth)
	}
}

@Composable
fun Modifier.adaptiveActionWidth(info: AdaptiveLayoutInfo): Modifier {
	return adaptiveActionWidth(
		info = info,
		maxWidth = LocalAdaptiveLayoutConfig.current.actionMaxWidth,
	)
}
