package ch.ubique.libs.kmpanion.compose.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A unified button composable where the entire styling and content can be customized with a DSL
 *
 * @param modifier The modifier applied to the button
 * @param shape The shape applied to the button and/or ripple
 * @param enabled True if the button is clickable, false if not. Button and content styling will change based on this flag
 * @param onClick The callback when the button is clicked
 * @param type The button type, customizable via the [ButtonTypeScope] DSL
 */
@Composable
fun UbiqueButton(
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.small,
	enabled: Boolean = true,
	padding: PaddingValues = ButtonDefaults.ContentPadding,
	onClick: () -> Unit,
	type: ButtonTypeScope.() -> Unit,
) {
	val config = ButtonConfig(modifier, shape, enabled, padding, onClick)
	val scopeImpl = ButtonTypeScopeImpl().apply(type)
	scopeImpl.getButtonType().itemContent.invoke(config)
}

/**
 * Custom scope for the button type
 */
@ButtonTypeScopeMarker
interface ButtonTypeScope {
	/**
	 * A filled and elevated button (see [Button])
	 */
	fun filled(
		colorProvider: @Composable () -> ButtonColors = { ButtonDefaults.buttonColors() },
		elevationProvider: @Composable () -> ButtonElevation = { ButtonDefaults.buttonElevation() },
		content: ButtonContentScope.() -> Unit,
	)

	/**
	 * An outlined button (see [OutlinedButton])
	 */
	fun outlined(
		colorProvider: @Composable () -> ButtonColors = { ButtonDefaults.outlinedButtonColors() },
		borderProvider: @Composable () -> BorderStroke = { ButtonDefaults.outlinedButtonBorder },
		content: ButtonContentScope.() -> Unit,
	)

	/**
	 * A text button without background or border (see [TextButton])
	 */
	fun text(
		colorProvider: @Composable () -> ButtonColors = { ButtonDefaults.textButtonColors() },
		content: ButtonContentScope.() -> Unit,
	)

	/**
	 * An icon only button (see [IconButton])
	 */
	fun icon(@DrawableRes iconId: Int, tintProvider: @Composable () -> Color = { LocalContentColor.current })
}

@DslMarker
private annotation class ButtonTypeScopeMarker

private class ButtonTypeScopeImpl : ButtonTypeScope {
	private var buttonType: ButtonTypeContent? = null

	fun getButtonType() = requireNotNull(buttonType)

	override fun filled(
		colorProvider: @Composable () -> ButtonColors,
		elevationProvider: @Composable () -> ButtonElevation,
		content: ButtonContentScope.() -> Unit,
	) {
		buttonType = ButtonTypeContent { config ->
			Button(
				modifier = config.modifier,
				shape = config.shape,
				enabled = config.enabled,
				colors = colorProvider.invoke(),
				elevation = elevationProvider.invoke(),
				contentPadding = config.padding,
				onClick = config.onClick,
			) {
				ButtonContent(config.enabled, content)
			}
		}
	}

	override fun outlined(
		colorProvider: @Composable () -> ButtonColors,
		borderProvider: @Composable () -> BorderStroke,
		content: ButtonContentScope.() -> Unit,
	) {
		buttonType = ButtonTypeContent { config ->
			OutlinedButton(
				modifier = config.modifier,
				shape = config.shape,
				enabled = config.enabled,
				colors = colorProvider.invoke(),
				border = borderProvider.invoke(),
				contentPadding = config.padding,
				onClick = config.onClick,
			) {
				ButtonContent(config.enabled, content)
			}
		}
	}

	override fun text(colorProvider: @Composable () -> ButtonColors, content: ButtonContentScope.() -> Unit) {
		buttonType = ButtonTypeContent { config ->
			TextButton(
				modifier = config.modifier,
				shape = config.shape,
				enabled = config.enabled,
				colors = colorProvider.invoke(),
				contentPadding = config.padding,
				onClick = config.onClick,
			) {
				ButtonContent(config.enabled, content)
			}
		}
	}

	override fun icon(iconId: Int, tintProvider: @Composable () -> Color) {
		buttonType = ButtonTypeContent { config ->
			IconButton(
				modifier = config.modifier,
				enabled = config.enabled,
				onClick = config.onClick,
			) {
				Icon(painterResource(iconId), contentDescription = null, tint = tintProvider.invoke())
			}
		}
	}

	@Composable
	private fun ButtonContent(enabled: Boolean, content: ButtonContentScope.() -> Unit) {
		val contentScopeImpl = ButtonContentScopeImpl().apply(content)
		Row(verticalAlignment = Alignment.CenterVertically) {
			contentScopeImpl.items.forEach {
				it.itemContent.invoke(this, enabled)
			}
		}
	}
}

private data class ButtonTypeContent(val itemContent: @Composable (ButtonConfig) -> Unit)

/**
 * Custom scope for the button content to prevent any custom code being invoked within the content
 */
@ButtonContentScopeMarker
interface ButtonContentScope {
	/**
	 * Adds a [Spacer] with the given [size]
	 */
	fun spacing(size: Dp)

	/**
	 * Adds a [CircularProgressIndicator] with the given [size], [strokeWidth] and color from the [colorProvider]
	 */
	fun loading(
		size: Dp = 24.dp,
		strokeWidth: Dp = 2.dp,
		colorProvider: @Composable (enabled: Boolean) -> Color = { LocalContentColor.current },
	)

	/**
	 * Adds a [Text] with the given [text], [maxLines] and [style]
	 */
	fun text(text: String, maxLines: Int = 1, style: @Composable () -> TextStyle = { MaterialTheme.typography.labelLarge })

	/**
	 * Adds an [Icon] with the given [iconId], [size] and [contentDescription]
	 */
	fun icon(@DrawableRes iconId: Int, size: Dp = 24.dp, contentDescription: String? = null)

	/**
	 * Adds an [Image] with the given [imageId] and [contentDescription]
	 */
	fun image(@DrawableRes imageId: Int, contentDescription: String? = null)
}

@DslMarker
private annotation class ButtonContentScopeMarker

private class ButtonContentScopeImpl : ButtonContentScope {
	val items = mutableListOf<ButtonContentItem>()

	override fun spacing(size: Dp) {
		items.add(ButtonContentItem { Spacer(Modifier.width(size)) })
	}

	override fun loading(size: Dp, strokeWidth: Dp, colorProvider: @Composable (Boolean) -> Color) {
		items.add(
			ButtonContentItem { enabled ->
				CircularProgressIndicator(
					modifier = Modifier.size(size),
					color = colorProvider.invoke(enabled),
					strokeWidth = strokeWidth,
				)
			}
		)
	}

	override fun text(text: String, maxLines: Int, style: @Composable () -> TextStyle) {
		items.add(ButtonContentItem { Text(text, style = style.invoke(), maxLines = maxLines, overflow = TextOverflow.Ellipsis, color = LocalContentColor.current) })
	}

	override fun icon(iconId: Int, size: Dp, contentDescription: String?) {
		items.add(ButtonContentItem { Icon(painterResource(iconId), contentDescription = contentDescription, modifier = Modifier.size(size)) })
	}

	override fun image(imageId: Int, contentDescription: String?) {
		items.add(ButtonContentItem { Image(painterResource(imageId), contentDescription = contentDescription) })
	}
}

private data class ButtonContentItem(val itemContent: @Composable RowScope.(enabled: Boolean) -> Unit)

private class ButtonConfig(
	val modifier: Modifier,
	val shape: Shape,
	val enabled: Boolean,
	val padding: PaddingValues,
	val onClick: () -> Unit,
)