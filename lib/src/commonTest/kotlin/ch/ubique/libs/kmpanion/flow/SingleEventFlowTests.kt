package ch.ubique.libs.kmpanion.flow

import kotlin.test.Test

class SingleEventFlowTests {

	@Test
	fun testSingleEventFlowApi() {
		val e = SingleEventFlow<Int?>()
		e.emit(42)
		e.emit(null)
	}

	@Test
	fun testSingleEventFlowUnitApi() {
		val e = SingleEventFlow<Unit>()
		e.emit(Unit)
		e.emit()
	}

}
