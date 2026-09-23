package ch.ubique.libs.kmpanion.compose.shimmer

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class ShimmerOptions(
	val colors: List<Color>,
	val width: ShimmerWidth,
	val cornerRadius: Dp,
	val shimmerAnimationSpec: AnimationSpec<Float>,
	val crossFadeAnimationSpec: AnimationSpec<Float>,
)