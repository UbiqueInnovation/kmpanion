package ch.ubique.libs.kmpanion.compose.adaptive.layout

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
import ch.ubique.libs.kmpanion.compose.adaptive.config.AdaptiveRole
import ch.ubique.libs.kmpanion.compose.adaptive.config.AdaptiveRoleConfig
import ch.ubique.libs.kmpanion.compose.adaptive.config.LocalAdaptiveRoleConfig

/**
 * A [LazyColumn] whose content width is limited by the policy for [role].
 *
 * @param info layout information used to select the applicable width limit
 * @param config configuration containing the policy for [role]
 * @param alignment horizontal position of the width-limited content
 */
@Composable
fun AdaptiveRoleLazyColumn(
	role: AdaptiveRole,
	modifier: Modifier = Modifier,

	// Custom parameters
	info: AdaptiveLayoutInfo = currentAdaptiveLayoutInfo(),
	config: AdaptiveRoleConfig = LocalAdaptiveRoleConfig.current,
	alignment: Alignment.Horizontal = Alignment.CenterHorizontally,

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
	SinglePaneLazyColumn(
		maxContentWidth = config.policyFor(role).resolveMaxWidth(info.widthClass),
		modifier = modifier,
		alignment = alignment,
		state = state,
		contentPadding = contentPadding,
		reverseLayout = reverseLayout,
		verticalArrangement = verticalArrangement,
		horizontalAlignment = horizontalAlignment,
		flingBehavior = flingBehavior,
		userScrollEnabled = userScrollEnabled,
		overscrollEffect = overscrollEffect,
		content = content,
	)
}

/**
 * A [LazyColumn] whose content is constrained to [maxContentWidth].
 *
 * @param alignment horizontal position of the width-limited content
 */
@Composable
fun SinglePaneLazyColumn(
	maxContentWidth: Dp,
	modifier: Modifier = Modifier,
	alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
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
		val extraHorizontalPadding = if (maxContentWidth == Dp.Unspecified) {
			0.dp
		} else {
			(maxWidth - horizontalContentPadding - maxContentWidth).coerceAtLeast(0.dp)
		}
		val horizontalPadding = when (alignment) {
			Alignment.Start -> PaddingValues(end = extraHorizontalPadding)
			Alignment.End -> PaddingValues(start = extraHorizontalPadding)
			else -> PaddingValues(horizontal = extraHorizontalPadding / 2)
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
