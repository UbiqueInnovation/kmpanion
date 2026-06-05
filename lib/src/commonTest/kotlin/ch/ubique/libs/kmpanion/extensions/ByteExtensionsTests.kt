package ch.ubique.libs.kmpanion.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class ByteExtensionsTests {

	@Test
	fun testToHexString() {
		assertEquals("ff", 0xFF.toByte().toHexString())
		assertEquals("00", 0x00.toByte().toHexString())
		assertEquals("0b", 0x0b.toByte().toHexString())
		assertEquals("c3", 0xc3.toByte().toHexString())
		assertEquals("8d", 0x8d.toByte().toHexString())
		assertEquals("e3", 0xe3.toByte().toHexString())
		assertEquals("ff", (-0x01).toByte().toHexString())
	}

}
