package ch.ubique.libs.kmpanion.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidStringExtensionsTests {

	@Test
	fun testToEnum() {
		val enum = "A".toEnum<TestEnum>()
		assertEquals(TestEnum.A, enum)
	}

	@Test
	fun testToEnumFallback() {
		val enum = "X".toEnum(fallback = TestEnum.B)
		assertEquals(TestEnum.B, enum)
	}

	@Test
	fun testToEnumIgnoreCase() {
		val enum = "b".toEnum<TestEnum>(ignoreCase = true)
		assertEquals(TestEnum.B, enum)
	}

	@Test
	fun testToEnumIllegal() {
		assertFailsWith<IllegalArgumentException> {
			"b".toEnum<TestEnum>()
		}
	}

	@Test
	fun testToEnumNullable() {
		val enum = "X".toEnumNullable<TestEnum>()
		assertNull(enum)
	}

}

private enum class TestEnum {
	A, B
}
