package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun AdaptiveContent(
	modifier: Modifier = Modifier,
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	config: AdaptiveLayoutConfig = LocalAdaptiveLayoutConfig.current,
	horizontalPadding: Dp = AdaptiveDefaults.contentPadding(info, config),
	maxWidth: Dp = AdaptiveDefaults.contentMaxWidth(info, config),
	contentAlignment: Alignment = Alignment.TopCenter,
	content: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = horizontalPadding),
		contentAlignment = contentAlignment,
	) {
		Box(
			modifier = Modifier.adaptiveMaxWidth(maxWidth),
			content = content,
		)
	}
}

@Composable
fun AdaptiveActionContent(
	modifier: Modifier = Modifier,
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	config: AdaptiveLayoutConfig = LocalAdaptiveLayoutConfig.current,
	horizontalPadding: Dp = AdaptiveDefaults.contentPadding(info, config),
	maxWidth: Dp = config.actionMaxWidth,
	content: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(PaddingValues(horizontal = horizontalPadding)),
		contentAlignment = Alignment.Center,
	) {
		Box(
			modifier = Modifier.adaptiveActionWidth(info = info, maxWidth = maxWidth),
			content = content,
		)
	}
}
