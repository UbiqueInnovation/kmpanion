package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import kotlin.math.min

@Composable
fun SinglePaneLayout(
	modifier: Modifier = Modifier,
	maxWidth: Dp = AdaptiveDefaults.singlePaneMaxWidth,
	alignment: Alignment = Alignment.TopCenter,
	content: @Composable () -> Unit,
) {
	Layout(
		modifier = modifier,
		content = content,
	) { measurables, constraints ->
		// Constrain the content to either the layout maxWidth or the specified maxWidth
		val maxWidthInPx = min(constraints.maxWidth, maxWidth.roundToPx())
		val newConstraints = Constraints(
			maxWidth = maxWidthInPx,
			maxHeight = constraints.maxHeight,
		)

		// Measure the content with the new constraints
		val placeables = measurables.map { it.measure(newConstraints) }

		// Determine the layout's width and height
		val contentHeight = placeables.sumOf { it.height }
		val layoutWidth = constraints.maxWidth
		val layoutHeight = if (constraints.hasBoundedHeight) {
			// If the height is bounded, take the smaller of either the content height or the max height
			minOf(contentHeight, constraints.maxHeight)
		} else {
			// If the height is unbounded, use the content height but ensure it's at least the min height
			contentHeight.coerceAtLeast(constraints.minHeight)
		}

		layout(layoutWidth, layoutHeight) {
			placeables.forEach { placeable ->
				val offsetX = when (alignment) {
					Alignment.Start,
					Alignment.TopStart,
					Alignment.CenterStart,
					Alignment.BottomStart,
						-> 0
					Alignment.End,
					Alignment.TopEnd,
					Alignment.CenterEnd,
					Alignment.BottomEnd,
						-> layoutWidth - placeable.width
					else -> (layoutWidth - placeable.width) / 2
				}

				val offsetY = when (alignment) {
					Alignment.CenterStart,
					Alignment.Center,
					Alignment.CenterEnd,
						-> (layoutHeight - placeable.height) / 2
					Alignment.Bottom,
					Alignment.BottomStart,
					Alignment.BottomCenter,
					Alignment.BottomEnd,
						-> layoutHeight - placeable.height
					else -> 0
				}

				placeable.placeRelative(x = offsetX, y = offsetY)
			}
		}
	}
}