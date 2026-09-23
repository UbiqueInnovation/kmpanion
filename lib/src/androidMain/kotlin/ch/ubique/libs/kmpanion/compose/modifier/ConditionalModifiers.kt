package ch.ubique.libs.kmpanion.compose.modifier

import androidx.compose.ui.Modifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Applies the [block] to [this] Modifier chain if [condition] is true
 */
@OptIn(ExperimentalContracts::class)
fun Modifier.ifTrue(
	condition: Boolean,
	block: Modifier.() -> Modifier,
): Modifier {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	return if (condition) {
		this.then(block())
	} else {
		this
	}
}

/**
 * Applies the [block] to [this] Modifier chain if [condition] evaluates to true
 */
@OptIn(ExperimentalContracts::class)
fun Modifier.ifTrue(
	condition: () -> Boolean,
	block: Modifier.() -> Modifier,
): Modifier {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	return this.ifTrue(condition(), block)
}

/**
 * Applies the [block] to [this] Modifier chain if [value] is not null, providing the non-null value as a parameter to [block]
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Modifier.ifNotNull(
	value: T?,
	block: Modifier.(T) -> Modifier,
): Modifier {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	return if (value != null) this.block(value) else this
}