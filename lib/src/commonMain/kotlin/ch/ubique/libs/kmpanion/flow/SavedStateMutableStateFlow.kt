package ch.ubique.libs.kmpanion.flow

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
class SavedStateMutableStateFlow<T> private constructor(
	private val savedStateHandle: SavedStateHandle,
	private val key: String,
	private val base: MutableStateFlow<T>,
) : MutableStateFlow<T> by base {

	@Suppress("UNCHECKED_CAST")
	constructor(savedStateHandle: SavedStateHandle, key: String, initialValue: T) : this(
		savedStateHandle,
		key,
		MutableStateFlow(
			if (savedStateHandle.contains(key)) savedStateHandle.get<T>(key) as T
			else initialValue.also { savedStateHandle[key] = initialValue }
		)
	)

	override var value: T
		get() = base.value
		set(value) {
			savedStateHandle[key] = value
			base.value = value
		}
}