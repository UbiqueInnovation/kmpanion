package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveRoleLazyColumn(
	role: AdaptiveRole,
	modifier: Modifier = Modifier,

	// Custom parameters
	info: AdaptiveLayoutInfo = rememberAdaptiveLayoutInfo(),
	config: AdaptiveRoleConfig = LocalAdaptiveRoleConfig.current,
	alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
	maxContentWidth: Dp = config.policyFor(role).resolveMaxWidth(info.widthClass),

	// Standard LazyColumn parameters (including their default values)
	state: LazyListState = rememberLazyListState(),
	contentPadding: PaddingValues = PaddingValues(0.dp),
	reverseLayout: Boolean = false,
	verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
	horizontalAlignment: Alignment.Horizontal = Alignment.Start,
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
	content: LazyListScope.() -> Unit,
) {
	val layoutDirection = LocalLayoutDirection.current
	BoxWithConstraints(modifier = modifier.fillMaxWidth(), propagateMinConstraints = true) {
		val horizontalContentPadding = contentPadding.calculateStartPadding(layoutDirection) +
				contentPadding.calculateEndPadding(layoutDirection)
		val availableWidth = if (maxContentWidth == Dp.Unspecified) {
			0.dp
		} else {
			(maxWidth - horizontalContentPadding - maxContentWidth).coerceAtLeast(0.dp)
		}
		val horizontalPadding = when (alignment) {
			Alignment.Start -> PaddingValues(end = availableWidth)
			Alignment.End -> PaddingValues(start = availableWidth)
			else -> PaddingValues(horizontal = availableWidth / 2)
		}

		val adaptiveContentPadding = contentPadding + horizontalPadding

		LazyColumn(
			modifier = Modifier.fillMaxWidth(),
			state = state,
			contentPadding = adaptiveContentPadding,
			reverseLayout = reverseLayout,
			verticalArrangement = verticalArrangement,
			horizontalAlignment = horizontalAlignment,
			flingBehavior = flingBehavior,
			userScrollEnabled = userScrollEnabled,
			overscrollEffect = overscrollEffect,
			content = content
		)
	}
}
