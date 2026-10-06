package ch.ubique.libs.kmpanion.compose.preview

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class MockTests {

	@Test
	fun testActionsAndObjectMethods() {
		val actions = mock<Actions>()
		actions.onClick()
		actions.onChange("value")
		assertTrue(actions == actions)
		assertFalse(actions == mock<Actions>())
		assertEquals(System.identityHashCode(actions), actions.hashCode())
		assertEquals("Mock<Actions>", actions.toString())
		assertFalse(actions.equals(42)) // An overload must not be mistaken for Object.equals.
	}

	@Test
	fun testPrimitiveDefaults() {
		val values = mock<Values>()
		assertFalse(values.boolean())
		assertEquals(0.toByte(), values.byte())
		assertEquals(0.toShort(), values.short())
		assertEquals(0, values.int())
		assertEquals(0L, values.long())
		assertEquals(0f, values.float())
		assertEquals(0.0, values.double())
		assertEquals('\u0000', values.char())
		assertEquals("", values.text)
		assertEquals(Unit, values.unit)
	}

	@Test
	fun testNullableReturnsUseTheSameDefaults() {
		val values = mock<Values>()
		assertEquals(0, values.nullableInt())
		assertEquals("", values.nullableText)
		assertEquals(emptyList(), values.nullableList())
		assertEquals(Unit, values.nullableUnit)
		assertFailsWith<IllegalStateException> { values.nullableRequired() }
	}

	@Test
	fun testCollectionsArraysAndConstructors() {
		val values = mock<Values>()
		assertTrue(values.list().isEmpty())
		assertTrue(values.set().isEmpty())
		assertTrue(values.map().isEmpty())
		assertTrue(values.collection().isEmpty())
		assertFalse(values.iterable().iterator().hasNext())
		assertTrue(values.array().isEmpty())
		assertTrue(values.intArray().isEmpty())
		val list = values.list()
		list.add("changed")
		assertTrue(values.list().isEmpty())
		assertTrue(values.concreteList().isEmpty())
		assertEquals("default", values.constructed().text)
		assertNotSame(values.constructed(), values.constructed())
	}

	@Test
	fun testUnsupportedReturnsFailWhenCalled() {
		val values = mock<Values>()
		val failure = assertFailsWith<IllegalStateException> { values.required() }
		assertTrue(failure.message.orEmpty().contains("Values.required"))
		assertFailsWith<IllegalStateException> { values.abstractValue() }
		assertFailsWith<IllegalStateException> { values.generic<String>() }
		assertFailsWith<IllegalStateException> { values.genericArray<String>() }
		assertFailsWith<IllegalArgumentException> { mock<DefaultConstructed>() }
	}

	@Test
	fun testSuspendMethodsFailClearly() = runTest {
		val failure = assertFailsWith<IllegalStateException> { mock<SuspendActions>().load() }
		assertTrue(failure.message.orEmpty().contains("suspend methods"))
	}

	interface SuspendActions {
		suspend fun load(): String?
	}

	interface Actions {
		fun onClick()
		fun onChange(value: String)
		fun equals(value: Int): Boolean
	}

	interface NullableProperties {
		val nullableText: String?
	}

	interface Values : NullableProperties {
		fun boolean(): Boolean
		fun byte(): Byte
		fun short(): Short
		fun int(): Int
		fun long(): Long
		fun float(): Float
		fun double(): Double
		fun char(): Char
		val text: String
		val unit: Unit
		val nullableUnit: Unit?
		fun nullableInt(): Int?
		fun nullableList(): List<String>?
		fun nullableRequired(): Required?
		fun list(): MutableList<String>
		fun set(): Set<String>
		fun map(): Map<String, Int>
		fun collection(): Collection<String>
		fun iterable(): Iterable<String>
		fun concreteList(): ArrayList<String>
		fun array(): Array<String>
		fun intArray(): IntArray
		fun constructed(): DefaultConstructed
		fun required(): Required
		fun abstractValue(): AbstractValue
		fun <T> generic(): T
		fun <T> genericArray(): Array<T>
	}

	class DefaultConstructed(val text: String = "default")
	class Required(val text: String)
	abstract class AbstractValue
}
