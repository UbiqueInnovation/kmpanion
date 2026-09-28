package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.ubique.libs.kmpanion.compose.preview.ScreenPreviews

fun Modifier.singlePaneWidth(
	maxWidth: Dp = AdaptiveDefaults.singlePaneMaxWidth,
): Modifier = this.then(
	SinglePaneWidthElement(
		maxWidth = maxWidth,
	)
)

private data class SinglePaneWidthElement(
	val maxWidth: Dp,
) : ModifierNodeElement<SinglePaneWidthModifier>() {

	override fun create() = SinglePaneWidthModifier(
		maxWidth = maxWidth,
	)

	override fun update(node: SinglePaneWidthModifier) {
		node.maxWidth = maxWidth
	}

	override fun InspectorInfo.inspectableProperties() {
		name = "singlePaneWidth"
		properties["maxWidth"] = maxWidth
	}
}

private class SinglePaneWidthModifier(
	maxWidth: Dp,
) : Modifier.Node(), LayoutModifierNode {

	var maxWidth: Dp = maxWidth
		set(value) {
			if (field == value) return
			field = value
		}

	override fun MeasureScope.measure(
		measurable: Measurable,
		constraints: Constraints,
	): MeasureResult {
		val availableWidth = constraints.maxWidth
		val constrainedWidth = availableWidth.coerceAtMost(maxWidth.roundToPx())

		val singlePaneConstraints = constraints.copy(minWidth = 0, maxWidth = constrainedWidth)
		val placeable = measurable.measure(singlePaneConstraints)

		return layout(availableWidth, placeable.height) {
			val spacing = (availableWidth - constrainedWidth) / 2
			placeable.place(spacing, 0)
		}
	}
}

@ScreenPreviews
@Composable
private fun SinglePaneWidthModifierPreview() {
	Box(
		modifier = Modifier
			.background(Color.Blue)
			.height(50.dp)
			.singlePaneWidth()
			.background(Color.Red)
	)
}