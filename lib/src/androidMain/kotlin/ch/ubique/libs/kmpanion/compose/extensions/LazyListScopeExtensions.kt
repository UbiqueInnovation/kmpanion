package ch.ubique.libs.kmpanion.compose.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Convenience extension function to add an item to a [LazyListScope] that automatically applies the [androidx.compose.foundation.lazy.LazyItemScope.animateItem] modifier
 */
fun LazyListScope.animatedItem(key: Any, content: @Composable LazyItemScope.() -> Unit) {
	item(key) {
		Box(Modifier.animateItem()) {
			content.invoke(this@item)
		}
	}
}

/**
 * Convenience extension function to add a list of items to a [LazyListScope] that automatically applies the [androidx.compose.foundation.lazy.LazyItemScope.animateItem] modifier to each item
 */
fun <T> LazyListScope.animatedItems(
	items: List<T>,
	key: (item: T) -> Any,
	contentType: (item: T) -> Any? = { null },
	content: @Composable LazyItemScope.(item: T) -> Unit,
) {
	items(items, key, contentType) { item ->
		Box(Modifier.animateItem()) {
			content.invoke(this@items, item)
		}
	}
}

/**
 * Convenience extension function to add a spacer item to a [LazyListScope]
 */
fun LazyListScope.spacer(size: Dp) {
	item {
		Spacer(Modifier.size(size))
	}
}