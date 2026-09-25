package ch.ubique.libs.kmpanion.compose.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.BINARY)
@Preview(
	name = "Compact",
	widthDp =  640,
	showBackground = true,
)
@Preview(
	name = "Medium",
	widthDp = 800,
	showBackground = true,
)
@Preview(
	name = "Expanded",
	widthDp = 1000,
	showBackground = true,
)
annotation class AdaptivePreviews

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.BINARY)
@Preview(
	name = "Compact Dark Mode",
	widthDp =  640,
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
	name = "Medium Dark Mode",
	widthDp = 800,
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
	name = "Expanded Dark Mode",
	widthDp = 1000,
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class AdaptiveDarkPreviews
