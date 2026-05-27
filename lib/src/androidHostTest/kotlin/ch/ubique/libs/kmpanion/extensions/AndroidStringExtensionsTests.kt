package ch.ubique.libs.kmpanion.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidStringExtensionsTests {

	@Test
	fun testMD5() {
		assertEquals("912ec803b2ce49e4a541068d495ab570", "asdf".md5())
		assertEquals("ad3424fdf72a46deb9e80976e616b25d", "Hello World!".repeat(512).md5())
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", "".md5())
	}

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
