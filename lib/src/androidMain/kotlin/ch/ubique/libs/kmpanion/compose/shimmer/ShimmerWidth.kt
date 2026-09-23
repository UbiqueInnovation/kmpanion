package ch.ubique.libs.kmpanion.compose.shimmer

import androidx.compose.ui.unit.Dp

sealed interface ShimmerWidth {
	/** Creates the shimmer based on the size constraints of the Composable it's applied to */
	data object Default : ShimmerWidth

	/** Fills the available width of the Composable it's applied to */
	data object FillMaxWidth : ShimmerWidth

	/** Creates the shimmer with a fixed width */
	data class FixedWidth(val width: Dp) : ShimmerWidth
}