package ch.ubique.libs.kmpanion.compose.shimmer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Creates a shimmer effect and crossfades between the shimmer and the actual content based on the [visible] flag.
 * Color, shape, width and animations of the shimmer are configurable via the [shimmerOptions].
 * Inspired by https://github.com/canerkaseler/jetpack-compose-shimmer-loading-animation
 */
fun Modifier.shimmer(
	visible: Boolean = true,
	shimmerOptions: ShimmerOptions = ShimmerDefaults.options(),
): Modifier = this.then(
	DrawShimmerElement(
		visible = visible,
		shimmerOptions = shimmerOptions
	)
)

private data class DrawShimmerElement(
	val visible: Boolean,
	val shimmerOptions: ShimmerOptions,
) : ModifierNodeElement<DrawShimmerModifier>() {

	override fun create() = DrawShimmerModifier(
		visible = visible,
		shimmerOptions = shimmerOptions,
	)

	override fun update(node: DrawShimmerModifier) {
		node.shimmerOptions = shimmerOptions
		node.visible = visible
	}

	override fun InspectorInfo.inspectableProperties() {
		name = "drawShimmer"
		properties["visible"] = visible
	}
}

private class DrawShimmerModifier(
	visible: Boolean,
	shimmerOptions: ShimmerOptions,
) : Modifier.Node(), DrawModifierNode, LayoutModifierNode {

	var visible: Boolean = visible
		set(value) {
			if (field == value) return
			field = value
			if (isAttached) {
				handleAnimations()
			}
		}

	var shimmerOptions: ShimmerOptions = shimmerOptions
		set(value) {
			if (field == value) return
			field = value
			if (isAttached) {
				invalidateDraw()
				if (visible) {
					handleAnimations()
				}
			}
		}

	private val shimmerVisibilityAnimatable = Animatable(0f)
	private val shimmerEffectAnimatable = Animatable(0f)

	private val contentLayerPaint = Paint()

	override fun onAttach() {
		super.onAttach()
		handleAnimations()
	}

	override fun ContentDrawScope.draw() {
		val contentAlpha = 1f - shimmerVisibilityAnimatable.value

		if (contentAlpha > 0f) {
			drawIntoCanvas { canvas ->
				canvas.saveLayer(
					bounds = size.toRect(),
					paint = contentLayerPaint.apply {
						alpha = contentAlpha
					}
				)
				drawContent()
				canvas.restore()
			}
		}

		if (shimmerVisibilityAnimatable.value > 0f) {
			animatedDraw(
				visibleAnimatable = shimmerVisibilityAnimatable,
				effectAnimatable = shimmerEffectAnimatable,
				shimmerOptions = shimmerOptions
			)
		}
	}

	override fun MeasureScope.measure(
		measurable: Measurable,
		constraints: Constraints,
	): MeasureResult {
		val childConstraints = when (val width = shimmerOptions.width) {
			is ShimmerWidth.Default -> constraints
			is ShimmerWidth.FillMaxWidth -> constraints.copy(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth)
			is ShimmerWidth.FixedWidth -> constraints.copy(minWidth = width.width.roundToPx(), maxWidth = width.width.roundToPx())
		}

		val placeable = measurable.measure(childConstraints)
		return layout(
			width = placeable.width,
			height = placeable.height
		) {
			placeable.place(0, 0)
		}
	}

	private fun handleAnimations() {
		if (visible) {
			coroutineScope.launch {
				launch {
					shimmerEffectAnimatable.snapTo(0f)
					shimmerEffectAnimatable.animateTo(
						targetValue = 1f,
						animationSpec = shimmerOptions.shimmerAnimationSpec
					)
				}
				launch {
					shimmerVisibilityAnimatable.animateTo(
						targetValue = 1f,
						animationSpec = shimmerOptions.crossFadeAnimationSpec
					)
				}
			}
		} else {
			coroutineScope.launch {
				launch {
					shimmerEffectAnimatable.stop()
				}
				launch {
					shimmerVisibilityAnimatable.animateTo(
						targetValue = 0f,
						animationSpec = shimmerOptions.crossFadeAnimationSpec
					)
				}
			}
		}
	}
}

private fun DrawScope.animatedDraw(
	visibleAnimatable: Animatable<Float, AnimationVector1D>? = null,
	effectAnimatable: Animatable<Float, AnimationVector1D>,
	shimmerOptions: ShimmerOptions,
) {
	val progress = effectAnimatable.value
	val colors = shimmerOptions.colors

	val brush = linearBrush(colors, progress)

	if (shimmerOptions.cornerRadius == 0.dp) {
		drawRect(
			brush = brush,
			size = size,
			alpha = visibleAnimatable?.value ?: 1f
		)
	} else {
		val cornerRadius = shimmerOptions.cornerRadius.toPx()
		drawRoundRect(
			brush = brush,
			size = size,
			cornerRadius = CornerRadius(cornerRadius, cornerRadius),
			alpha = visibleAnimatable?.value ?: 1f,
		)
	}
}

private fun DrawScope.linearBrush(
	colors: List<Color>,
	progress: Float,
): Brush {
	val shimmerScale = 1.5f
	val shimmerWidth = size.width * shimmerScale
	val skewCorrection = size.height * shimmerScale * 4
	val offset = progress * (size.width + shimmerWidth * 2) - shimmerWidth - skewCorrection

	return Brush.linearGradient(
		colors = colors,
		start = Offset(offset, 0f),
		end = Offset(offset + shimmerWidth, size.height * shimmerScale * 4),
	)
}

@Preview(showBackground = true)
@Composable
private fun ShimmerModifierPreview() {
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier.padding(16.dp),
	) {
		Box(
			modifier = Modifier
				.size(width = 100.dp, height = 20.dp)
				.shimmer()
		)

		Box(
			modifier = Modifier
				.size(width = 100.dp, height = 20.dp)
				.shimmer(
					shimmerOptions = ShimmerDefaults.options(width = ShimmerWidth.FillMaxWidth)
				)
		)

		Box(
			modifier = Modifier
				.size(width = 100.dp, height = 20.dp)
				.shimmer(
					shimmerOptions = ShimmerDefaults.options(width = ShimmerWidth.FixedWidth(50.dp))
				)
		)

		Text(
			text = "This is a text with a shimmer effect",
			modifier = Modifier
				.size(width = 100.dp, height = 20.dp)
				.shimmer()
		)
	}
}