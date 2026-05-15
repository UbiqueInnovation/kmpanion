package ch.ubique.libs.kmpanion.compose.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


/**
 * A MultiPreview annotation for previewing component composables
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
	name = "Light",
	group = "Light",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_LIGHT_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
	name = "Dark",
	group = "Dark",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_DARK_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ComponentPreviews
