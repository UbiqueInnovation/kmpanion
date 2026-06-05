package ch.ubique.libs.kmpanion.compose.toggleable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ch.ubique.libs.kmpanion.compose.extensions.alpha

/**
 * Convert a boolean value to a [ToggleableState]
 */
fun Boolean.toToggleableState() = if (this) ToggleableState.On else ToggleableState.Off

/**
 * Convert a [ToggleableState] to a boolean, with a fallback [indeterminateValue] in case the state is [ToggleableState.Indeterminate]
 */
fun ToggleableState.toBoolean(indeterminateValue: Boolean = false) = when (this) {
	ToggleableState.On -> true
	ToggleableState.Off -> false
	ToggleableState.Indeterminate -> indeterminateValue
}

/**
 * A toggleable composable where the entire container is clickable and exposed to accessibility services as a toggleable element.
 *
 * @param modifier The modifier applied to the toggleable container
 * @param toggleState The current toggle state of the toggleable
 * @param enabled True if the toggleable is enabled and can be interacted with
 * @param contentPadding The content padding of the toggleable container (applied after the ripple effect)
 * @param verticalAlignment The alignment of the toggleable content inside the row
 * @param onClick The callback when the toggleable is clicked, passing in the current state so the caller can update its state
 * @param content The toggleable content, customizable via the [ToggleableScope] DSL
 */
@Composable
fun Toggleable(
	modifier: Modifier = Modifier,
	toggleState: ToggleableState,
	enabled: Boolean = true,
	contentPadding: PaddingValues = PaddingValues(5.dp),
	verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
	onClick: (current: ToggleableState) -> Unit,
	content: ToggleableScope.() -> Unit,
) {
	Row(
		modifier = modifier
			.triStateToggleable(
				state = toggleState,
				enabled = enabled,
				onClick = { onClick.invoke(toggleState) },
				role = Role.Checkbox,
			)
			.padding(contentPadding),
		verticalAlignment = verticalAlignment,
	) {
		val scopeContent = ToggleableScopeImpl().apply(content)
		scopeContent.items.forEach {
			it.itemContent.invoke(this, enabled)
		}
	}
}

/**
 * Custom scope for the toggleable content to prevent any custom code being invoked within the content
 */
@ToggleableScopeMarker
interface ToggleableScope {
	/**
	 * Displays a regular checkbox representing the [toggleState] and using the [CheckboxColors] returned by the [colorProvider]
	 */
	fun checkbox(toggleState: ToggleableState, colorProvider: @Composable () -> CheckboxColors = { CheckboxDefaults.colors() })

	/**
	 * Displays a regular switch that is [checked] or not and using the [SwitchColors] returned by the [colorProvider]. An optional [thumbContent] can be passed as well
	 */
	fun switch(
		checked: Boolean,
		thumbContent: @Composable (() -> Unit)? = null,
		colorProvider: @Composable () -> SwitchColors = { SwitchDefaults.colors() },
	)

	/**
	 * Adds a [Spacer] with the given [size]
	 */
	fun spacing(size: Dp)

	/**
	 * Calls the [content] composable lambda
	 */
	fun label(content: @Composable RowScope.(enabled: Boolean) -> Unit)

	/**
	 * Displays [text] with the given [textStyle]
	 */
	fun label(text: String, textStyle: TextStyle, modifier: Modifier = Modifier)
}

/**
 * Annotation to denote the [ToggleableScope] as a DSL
 */
@DslMarker
private annotation class ToggleableScopeMarker

private class ToggleableScopeImpl : ToggleableScope {
	val items = mutableListOf<ToggleableItem>()

	override fun checkbox(toggleState: ToggleableState, colorProvider: @Composable () -> CheckboxColors) {
		items.add(ToggleableItem {
			TriStateCheckbox(
				state = toggleState,
				onClick = null,
				enabled = it,
				colors = colorProvider.invoke()
			)
		})
	}

	override fun switch(
		checked: Boolean,
		thumbContent: @Composable (() -> Unit)?,
		colorProvider: @Composable () -> SwitchColors,
	) {
		items.add(ToggleableItem {
			Switch(
				checked = checked,
				onCheckedChange = null,
				enabled = it,
				thumbContent = thumbContent,
				colors = colorProvider.invoke()
			)
		})
	}

	override fun spacing(size: Dp) {
		items.add(ToggleableItem { Spacer(Modifier.width(size)) })
	}

	override fun label(content: @Composable RowScope.(enabled: Boolean) -> Unit) {
		items.add(ToggleableItem(content))
	}

	override fun label(text: String, textStyle: TextStyle, modifier: Modifier) {
		items.add(
			ToggleableItem { enabled ->
				val contentAlpha = if (enabled) 1f else 0.38f
				CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.alpha(contentAlpha)) {
					Text(text, style = textStyle, modifier = modifier)
				}
			}
		)
	}
}

/**
 * Data container for toggleable item content
 */
private data class ToggleableItem(val itemContent: @Composable RowScope.(enabled: Boolean) -> Unit)


@Preview(showBackground = true)
@Composable
private fun ToggleablePreview() {
	MaterialTheme {
		Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
			val textStyle = MaterialTheme.typography.titleSmall

			val checkboxState = remember { mutableStateOf(ToggleableState.Off) }
			Toggleable(
				modifier = Modifier.clip(RoundedCornerShape(5.dp)),
				toggleState = checkboxState.value,
				onClick = {
					checkboxState.value = when (it) {
						ToggleableState.On -> ToggleableState.Off
						ToggleableState.Indeterminate -> ToggleableState.On
						ToggleableState.Off -> ToggleableState.Indeterminate
					}
				}
			) {
				checkbox(checkboxState.value) { CheckboxDefaults.colors() }
				spacing(size = 10.dp)
				label { Text("Check me", style = textStyle) }
			}

			val switchState = remember { mutableStateOf(ToggleableState.Off) }
			Toggleable(
				modifier = Modifier.clip(RoundedCornerShape(5.dp)),
				toggleState = switchState.value,
				onClick = {
					switchState.value = when (it) {
						ToggleableState.On -> ToggleableState.Off
						else -> ToggleableState.On
					}
				}
			) {
				label("Switch me", textStyle)
				spacing(size = 10.dp)
				switch(switchState.value.toBoolean()) { SwitchDefaults.colors() }
			}

			Toggleable(
				modifier = Modifier.clip(RoundedCornerShape(5.dp)),
				toggleState = switchState.value,
				onClick = {
					switchState.value = when (it) {
						ToggleableState.On -> ToggleableState.Off
						else -> ToggleableState.On
					}
				}
			) {
				label("Switch me", textStyle)
				spacing(size = 10.dp)
				switch(
					switchState.value.toBoolean(),
					thumbContent = {
						val text = if (switchState.value.toBoolean()) "On" else "Off"
						Text(text, style = MaterialTheme.typography.bodySmall)
					},
					colorProvider = { SwitchDefaults.colors() }
				)
			}
		}
	}
}
