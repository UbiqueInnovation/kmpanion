package ch.ubique.libs.kmpanion.compose.swipeable

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableContainer(
	modifier: Modifier = Modifier,
	draggableState: AnchoredDraggableState<DragAnchor> = rememberDraggableState(),
	canSwipeToLeft: Boolean = false,
	canSwipeToRight: Boolean = false,
	onSwipedToLeft: () -> Unit = {},
	onSwipedToRight: () -> Unit = {},
	swipeToLeftContent: @Composable BoxScope.() -> Unit = {},
	swipeToRightContent: @Composable BoxScope.() -> Unit = {},
	content: @Composable BoxScope.() -> Unit,
) {
	val density = LocalDensity.current
	val windowInfo = LocalWindowInfo.current

	var contentSizePx by remember {
		val screenWidthPx = with(density) { windowInfo.containerSize.width.toDp().roundToPx() }
		mutableStateOf(IntSize(screenWidthPx, 0))
	}
	val contentSizeDp = remember(contentSizePx) {
		with(density) { DpSize(contentSizePx.width.toDp(), contentSizePx.height.toDp()) }
	}

	val centerAnchorPx = 0f
	val startAnchorPx = remember(contentSizePx) { -contentSizePx.width.toFloat() }
	val endAnchorPx = remember(contentSizePx) { contentSizePx.width.toFloat() }

	LaunchedEffect(draggableState.settledValue) {
		when (draggableState.settledValue) {
			DragAnchor.START -> onSwipedToLeft()
			DragAnchor.CENTER -> {
				// Do nothing
			}
			DragAnchor.END -> onSwipedToRight()
		}
	}

	Box(
		modifier = modifier
			.onSizeChanged { size ->
				contentSizePx = size
				draggableState.updateAnchors(
					DraggableAnchors {
						if (canSwipeToLeft) DragAnchor.START at startAnchorPx
						DragAnchor.CENTER at centerAnchorPx
						if (canSwipeToRight) DragAnchor.END at endAnchorPx
					}
				)
			}
			.anchoredDraggable(
				state = draggableState,
				orientation = Orientation.Horizontal,
			)
	) {
		if (canSwipeToLeft && draggableState.offset < centerAnchorPx) {
			Box(modifier = Modifier.size(contentSizeDp)) {
				swipeToLeftContent()
			}
		}
		if (canSwipeToRight && draggableState.offset > centerAnchorPx) {
			Box(modifier = Modifier.size(contentSizeDp)) {
				swipeToRightContent()
			}
		}

		Box(
			modifier = Modifier.offset {
				val xOffset = draggableState.offset.takeUnless { it.isNaN() }?.roundToInt() ?: 0
				IntOffset(xOffset, 0)
			},
			content = content,
		)
	}
}

@Composable
fun rememberDraggableState(initialValue: DragAnchor = DragAnchor.CENTER): AnchoredDraggableState<DragAnchor> {
	val density = LocalDensity.current

	return remember {
		AnchoredDraggableState(
			initialValue = initialValue,
			positionalThreshold = { distance -> distance * 0.5f },
			velocityThreshold = { with(density) { 1500.dp.toPx() } },
			snapAnimationSpec = tween(),
			decayAnimationSpec = exponentialDecay(),
		)
	}
}