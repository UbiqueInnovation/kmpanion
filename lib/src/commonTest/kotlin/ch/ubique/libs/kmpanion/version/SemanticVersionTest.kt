package ch.ubique.libs.kmpanion.version

import kotlin.test.*

class SemanticVersionTest {

	@Test
	fun testSmaller() {
		val new = SemanticVersion("1.2")
		val old = SemanticVersion("1.1.45")
		assertTrue(old < new)
	}

	@Test
	fun testSmallerEquals() {
		val new = SemanticVersion("1.2")
		val old = SemanticVersion("1.2")
		assertTrue(old <= new)
	}

	@Test
	fun testEquals() {
		val new = SemanticVersion("1.2.3")
		val old = SemanticVersion("1.2.3")
		assertEquals(old, new)
	}

	@Test
	fun testEqualsZeroes() {
		val new = SemanticVersion("1.2")
		val old = SemanticVersion("1.2.0")
		assertEquals(old, new)
	}

	@Test
	fun testNotEquals() {
		val new = SemanticVersion("1.2.4")
		val old = SemanticVersion("1.2.3")
		assertTrue(old != new)
	}

	@Test
	fun testInverse() {
		val new = SemanticVersion("0.2")
		val old = SemanticVersion("0.1.3")
		assertEquals(old < new, new >= old)
	}

	@Test
	fun testHashcodeZeroes() {
		val new = SemanticVersion("1.2")
		val old = SemanticVersion("1.2.0")
		assertEquals(old.hashCode(), new.hashCode())
	}

	@Test
	fun testEqualsNull() {
		val new = SemanticVersion("1.2")
		val old: SemanticVersion? = null
		assertNotEquals(old, new)
	}

	@Test
	fun testEqualsAny() {
		val new = SemanticVersion("1.2")
		val old = Any()
		assertNotEquals(old, new)
	}

	@Test
	fun testInvalid1() {
		assertFailsWith<IllegalArgumentException> {
			SemanticVersion("1.2.")
		}
	}

	@Test
	fun testInvalid2() {
		assertFailsWith<IllegalArgumentException> {
			SemanticVersion("foobarz")
		}
	}

}