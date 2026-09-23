package ch.ubique.libs.kmpanion.compose.shimmer

import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ShimmerDefaults {

	fun options(
		colors: List<Color> = lightShimmerColors(),
		width: ShimmerWidth = ShimmerWidth.Default,
		cornerRadius: Dp = 0.dp,
		shimmerAnimationSpec: AnimationSpec<Float> = infiniteRepeatable(
			animation = tween(1600, easing = FastOutLinearInEasing),
			repeatMode = RepeatMode.Restart
		),
		crossFadeAnimationSpec: AnimationSpec<Float> = tween(300),
	): ShimmerOptions = ShimmerOptions(
		colors = colors,
		width = width,
		cornerRadius = cornerRadius,
		shimmerAnimationSpec = shimmerAnimationSpec,
		crossFadeAnimationSpec = crossFadeAnimationSpec,
	)

	fun lightShimmerColors(): List<Color> = listOf(
		Color.Transparent,
		Color.White.copy(alpha = 0.1f),
		Color.White.copy(alpha = 0.2f),
		Color.White.copy(alpha = 0.3f),
		Color.White.copy(alpha = 0.66f),
		Color.Transparent,
	)

	fun darkShimmerColors(): List<Color> = listOf(
		Color.Transparent,
		Color.DarkGray.copy(alpha = 0.1f),
		Color.DarkGray.copy(alpha = 0.2f),
		Color.DarkGray.copy(alpha = 0.3f),
		Color.DarkGray.copy(alpha = 0.66f),
		Color.Transparent,
	)

}