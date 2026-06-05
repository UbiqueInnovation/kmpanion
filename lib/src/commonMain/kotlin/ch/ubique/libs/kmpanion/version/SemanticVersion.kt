package ch.ubique.libs.kmpanion.version

import kotlin.math.max

private val versionFormat = "[0-9]+(\\.[0-9]+)*".toRegex()

/**
 * Data class to compare versions strings.
 * @param version `major.minor.revision` or similar formats
 */
class SemanticVersion(val version: String) : Comparable<SemanticVersion> {

	init {
		require(version.matches(versionFormat)) { "Invalid version format" }
	}

	override operator fun compareTo(other: SemanticVersion): Int {
		val myParts = this.version.split('.')
		val yourParts = other.version.split('.')
		val length = max(myParts.size, yourParts.size)
		for (i in 0 until length) {
			val myPart = if (i < myParts.size) myParts[i].toInt() else 0
			val yourPart = if (i < yourParts.size) yourParts[i].toInt() else 0
			if (myPart < yourPart) return -1
			if (myPart > yourPart) return 1
		}
		return 0
	}

	override fun equals(other: Any?): Boolean {
		return (other as? SemanticVersion?)?.compareTo(this)?.equals(0) ?: false
	}

	override fun hashCode(): Int {
		return version.trimEnd('.', '0').hashCode()
	}

	override fun toString(): String {
		return version
	}

}