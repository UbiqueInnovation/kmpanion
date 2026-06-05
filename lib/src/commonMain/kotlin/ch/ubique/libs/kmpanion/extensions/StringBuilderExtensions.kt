package ch.ubique.libs.kmpanion.extensions

fun StringBuilder.appendNonNull(value: String?) {
	if (value != null) append(value)
}
