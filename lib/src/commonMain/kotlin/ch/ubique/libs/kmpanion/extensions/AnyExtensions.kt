package ch.ubique.libs.kmpanion.extensions

/**
 * Calls the specified function [block] with `this` value as its receiver if [condition] is true, and returns `this` value.
 */
inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
	if (condition) {
		block()
	}
	return this
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result if [condition] is true, or `this` otherwise.
 */
inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
	return if (condition) block() else this
}

/**
 * Cast a value to the specified type `T`.
 *
 * Example: `obj.cast<String>()`
 */
inline fun <reified T> Any.cast(): T {
	return this as T
}

/**
 * Cast a value to the specified type `T`. Returns null if it can't be cast to `T`.
 *
 * Example: `obj.castOrNull<String>()`
 */
inline fun <reified T> Any.castOrNull(): T? {
	return this as? T
}

/**
 * Throws an [IllegalArgumentException] with the result of calling [lazyMessage] if [this] value is null. Otherwise, returns the not null value.
 */
inline fun <T : Any> T?.requireNotNull(lazyMessage: () -> String = { "Required value was null." }): T {
	return requireNotNull(this, lazyMessage)
}
