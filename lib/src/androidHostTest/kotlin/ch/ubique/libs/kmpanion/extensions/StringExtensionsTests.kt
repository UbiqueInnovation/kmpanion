package ch.ubique.libs.kmpanion.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class StringExtensionsTests {

	@Test
	fun testReplaceAccentedLetters() {
		assertEquals(("äàáâãåā").unaccent(), "aaaaaaa")
		assertEquals(("éèêëė").unaccent(), "eeeee")
		assertEquals(("îïíīì").unaccent(), "iiiii")
		assertEquals(("öôòóõō").unaccent(), "oooooo")
		assertEquals(("üûùúū").unaccent(), "uuuuu")
	}

	@Test
	fun testKeepUnaccentedLetters() {
		assertEquals(("abcdefghijklmnopqrstuvwxyz").unaccent(), "abcdefghijklmnopqrstuvwxyz")
		assertEquals(("ABCDEFGHIJKLMNOPQRSTUVWXYZ").unaccent(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

		assertEquals((".!?").unaccent(), ".!?")

		assertEquals("æ".unaccent(), "æ")
		assertEquals("œø".unaccent(), "œø")
	}

}
