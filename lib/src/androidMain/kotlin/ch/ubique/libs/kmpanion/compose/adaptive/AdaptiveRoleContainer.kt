package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp


@Composable
fun AdaptiveRoleContainer(
	role: AdaptiveRole,
	modifier: Modifier = Modifier,
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	config: AdaptiveRoleConfig = LocalAdaptiveRoleConfig.current,
	content: @Composable BoxScope.() -> Unit,
) {
	val maxWidth = config.policyFor(role).resolveMaxWidth(info.widthClass)
	val widthModifier = if (maxWidth == Dp.Unspecified) {
		Modifier.fillMaxWidth()
	} else {
		Modifier.requiredWidthIn(max = maxWidth).fillMaxWidth()
	}

	Box(
		modifier = modifier.fillMaxWidth(),
		contentAlignment = Alignment.TopCenter,
	) {
		Box(modifier = widthModifier, content = content)
	}
}