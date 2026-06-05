package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

/**
 * A wrapper around a [LazyColumn] that adds additional horizontal padding to the [contentPadding] to ensure the content is at most
 * [maxContentWidth] wide, and also respects the given [alignment].
 */
@Composable
fun SinglePaneLazyColumn(
	modifier: Modifier = Modifier,

	// Custom parameters
	maxContentWidth: Dp = AdaptiveDefaults.singlePaneMaxWidth,
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
	BoxWithConstraints {
		val availableWidth = (maxWidth - maxContentWidth).coerceAtLeast(0.dp)
		val horizontalPadding = when (alignment) {
			Alignment.Start -> PaddingValues(end = availableWidth)
			Alignment.End -> PaddingValues(start = availableWidth)
			else -> PaddingValues(horizontal = availableWidth / 2)
		}

		val singlePaneContentPadding = contentPadding + horizontalPadding

		LazyColumn(
			modifier = modifier,
			state = state,
			contentPadding = singlePaneContentPadding,
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
