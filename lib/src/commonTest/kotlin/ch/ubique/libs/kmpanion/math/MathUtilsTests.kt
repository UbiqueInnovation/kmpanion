package ch.ubique.libs.kmpanion.math

import kotlin.test.Test
import kotlin.test.assertEquals

class MathUtilsTests {

	@Test
	fun testEuclideanDistance() {
		val distance = euclideanDistance(0f, 3f, 4f, 0f)
		assertEquals(5.0f, distance, 0.00000001f)
	}

}