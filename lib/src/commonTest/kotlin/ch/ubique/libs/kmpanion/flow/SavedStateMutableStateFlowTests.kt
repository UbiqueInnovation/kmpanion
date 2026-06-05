package ch.ubique.libs.kmpanion.flow

import androidx.lifecycle.SavedStateHandle
import kotlin.test.Test
import kotlin.test.assertEquals

class SavedStateMutableStateFlowTests {

	@Test
	fun testInitializedWithDefault() {
		val key = "k42"
		val defaultValue = 12
		val savedStateHandle = SavedStateHandle()
		val flow = SavedStateMutableStateFlow(savedStateHandle, key, defaultValue)
		assertEquals(defaultValue, flow.value)
	}

	@Test
	fun testInitializedWithSaved() {
		val key = "k42"
		val storedValue = 34
		val defaultValue = 12
		val savedStateHandle = SavedStateHandle()
		savedStateHandle[key] = storedValue
		val flow = SavedStateMutableStateFlow(savedStateHandle, key, defaultValue)
		assertEquals(storedValue, flow.value)
	}

	@Test
	fun testChangedValue() {
		val key = "k42"
		val defaultValue = 12
		val changedValue = 34
		val savedStateHandle = SavedStateHandle()
		val flow = SavedStateMutableStateFlow(savedStateHandle, key, defaultValue)
		flow.value = changedValue
		assertEquals(changedValue, flow.value)
		assertEquals(changedValue, savedStateHandle[key])
	}

}
