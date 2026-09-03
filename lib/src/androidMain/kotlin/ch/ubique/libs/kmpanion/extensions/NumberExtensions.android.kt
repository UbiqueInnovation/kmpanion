package ch.ubique.libs.kmpanion.extensions

import android.content.Context
import android.util.TypedValue

fun Number.dpToPx(context: Context): Float {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)
}

fun Number.spToPx(context: Context): Float {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics)
}

fun Number.pxToDp(context: Context): Float {
	return toFloat() / context.resources.displayMetrics.density
}