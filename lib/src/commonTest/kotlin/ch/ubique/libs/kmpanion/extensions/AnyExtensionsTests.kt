package ch.ubique.libs.kmpanion.extensions

import kotlin.test.*

class AnyExtensionsTests {

	@Test
	fun testCast() {
		val str: Any = "test"
		val cast = str.cast<String>()
		assertEquals(str, cast)
	}

	@Test
	fun testCastFail() {
		val str = "test"
		assertFailsWith<ClassCastException> {
			str.cast<Int>()
		}
	}

	@Test
	fun testCastOrNull() {
		val str = "test"
		val cast = str.castOrNull<Int>()
		assertNull(cast)
	}

	@Test
	fun testRequireNotNull() {
		val str = "test"
		val notNull = str.requireNotNull()
		assertNotNull(notNull)
	}

	@Test
	fun testRequireNotNullFail() {
		val str: String? = null
		assertFailsWith<IllegalArgumentException> {
			str.requireNotNull()
		}
	}

	@Test
	fun testRequireNotNullCustomError() {
		val str: String? = null
		val e = assertFailsWith<IllegalArgumentException> {
			str.requireNotNull { "oh noes!" }
		}
		assertEquals("oh noes!", e.message)
	}
}
