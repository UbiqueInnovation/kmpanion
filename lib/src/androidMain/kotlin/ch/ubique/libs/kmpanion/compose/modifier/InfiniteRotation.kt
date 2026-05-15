package ch.ubique.libs.kmpanion.compose.modifier

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A custom modifier that will rotate the element infinitely.
 * [composed] is required to create a stateful modifier (see https://developer.android.com/reference/kotlin/androidx/compose/ui/package-summary#(androidx.compose.ui.Modifier).composed(kotlin.Function1,%20kotlin.Function1))
 */
fun Modifier.infiniteRotation(
	durationPerRotationInMs: Int = 1500,
): Modifier = composed {
	val infiniteTransition = rememberInfiniteTransition(label = "infinity")
	val angle by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(animation = tween(durationPerRotationInMs, easing = LinearEasing)),
		label = "rotation",
	)

	this.graphicsLayer { rotationZ = angle }
}

@Preview(showBackground = true)
@Composable
private fun InfiniteRotatingComposablePreview() {
	Text(
		"They see me rolling",
		modifier = Modifier
			.padding(100.dp)
			.infiniteRotation()
	)
}