package ch.ubique.libs.kmpanion.compose.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * A MultiPreview annotation for displaying a @Composable on a landscape phone in light and dark mode
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
	name = "Phone - Portrait - Light Mode",
	device = "spec:parent=pixel_9,orientation=portrait",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_LIGHT_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
	name = "Phone - Portrait - Dark Mode",
	device = "spec:parent=pixel_9,orientation=portrait",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_DARK_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class PhonePortraitPreviews

/**
 * A MultiPreview annotation for displaying a @Composable on a phone tablet in light and dark mode
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
	name = "Phone - Landscape - Light Mode",
	device = "spec:parent=pixel_9,orientation=landscape",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_LIGHT_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
	name = "Phone - Landscape - Dark Mode",
	device = "spec:parent=pixel_9,orientation=landscape",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_DARK_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class PhoneLandscapePreviews

/**
 * A MultiPreview annotation combining the [PhonePortraitPreviews] and [PhoneLandscapePreviews] annotations
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PhonePortraitPreviews
@PhoneLandscapePreviews
annotation class PhonePreviews

/**
 * A MultiPreview annotation for displaying a @Composable on a portrait tablet in light and dark mode
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
	name = "Tablet - Portrait - Light Mode",
	device = "spec:parent=pixel_tablet,orientation=portrait",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_LIGHT_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
	name = "Tablet - Portrait - Dark Mode",
	device = "spec:parent=pixel_tablet,orientation=portrait",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_DARK_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class TabletPortraitPreviews

/**
 * A MultiPreview annotation for displaying a @Composable on a landscape tablet in light and dark mode
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
	name = "Tablet - Landscape - Light Mode",
	device = "spec:parent=pixel_tablet,orientation=landscape",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_LIGHT_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
	name = "Tablet - Landscape - Dark Mode",
	device = "spec:parent=pixel_tablet,orientation=landscape",
	showBackground = true,
	backgroundColor = PreviewDefaults.BACKGROUND_DARK_MODE,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class TabletLandscapePreviews

/**
 * A MultiPreview annotation combining the [TabletPortraitPreviews] and [TabletLandscapePreviews] annotations
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@TabletPortraitPreviews
@TabletLandscapePreviews
annotation class TabletPreviews

/**
 * A MultiPreview annotation for previewing screen level composables
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PhonePreviews
@TabletPreviews
annotation class ScreenPreviews
