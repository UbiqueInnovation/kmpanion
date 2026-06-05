package ch.ubique.libs.kmpanion.compose.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun Modifier.tripleClick(
	intervalMillis: Long = 250L,
	onTripleClick: () -> Unit,
) = this.multiClick(
	count = 3,
	intervalMillis = intervalMillis,
	onMultiClick = onTripleClick
)

fun Modifier.multiClick(
	count: Int,
	intervalMillis: Long = 250L,
	onMultiClick: () -> Unit,
): Modifier {
	return this then MultiClickElement(
		count = count,
		intervalMillis = intervalMillis,
		onMultiClick = onMultiClick,
	)
}

private data class MultiClickElement(
	val count: Int,
	val intervalMillis: Long,
	val onMultiClick: () -> Unit,
) : ModifierNodeElement<MultiClickNode>() {
	override fun create() = MultiClickNode(count, intervalMillis, onMultiClick)

	override fun update(node: MultiClickNode) {
		node.count = this.count
		node.intervalMillis = this.intervalMillis
		node.onMultiClick = this.onMultiClick
	}

	override fun InspectorInfo.inspectableProperties() {
		name = "multiClick"
		properties["count"] = count
		properties["intervalMillis"] = intervalMillis
		properties["onMultiClick"] = onMultiClick
	}
}

private class MultiClickNode(
	var count: Int,
	var intervalMillis: Long,
	var onMultiClick: () -> Unit,
) : Modifier.Node(), PointerInputModifierNode {

	private var clickCount = 0
	private var lastClickTime = 0L

	override fun onPointerEvent(pointerEvent: PointerEvent, pass: PointerEventPass, bounds: IntSize) {
		// Only handle the main pass of pointer events
		if (pass != PointerEventPass.Main) return

		// Only handle pointer release events for counting clicks
		if (pointerEvent.type != PointerEventType.Release) return

		val currentTime = System.currentTimeMillis()
		if (currentTime - lastClickTime <= intervalMillis) {
			clickCount++
		} else {
			clickCount = 1
		}

		lastClickTime = currentTime

		if (clickCount == count) {
			onMultiClick.invoke()
			clickCount = 0
		}
	}

	override fun onCancelPointerInput() {
		clickCount = 0
	}
}

@Preview(showBackground = true)
@Composable
private fun TripleClickModifierPreview() {
	var tripleClickCount by remember { mutableIntStateOf(0) }

	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.tripleClick { tripleClickCount++ }
			.padding(8.dp),
	) {
		Text("Triple Click Count: $tripleClickCount")
	}
}

@Preview(showBackground = true)
@Composable
private fun MultiClickModifierPreview() {
	var multiClickCount by remember { mutableIntStateOf(0) }

	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.multiClick(5) { multiClickCount++ }
			.padding(8.dp),
	) {
		Text("Multi Click Count: $multiClickCount")
	}
}