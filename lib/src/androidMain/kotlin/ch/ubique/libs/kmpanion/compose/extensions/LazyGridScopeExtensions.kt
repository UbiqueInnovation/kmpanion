package ch.ubique.libs.kmpanion.compose.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Convenience extension function to add an item to a [LazyGridScope] that automatically applies the [androidx.compose.foundation.lazy.grid.LazyGridItemScope.animateItem] modifier
 */
fun LazyGridScope.animatedItem(key: Any, content: @Composable LazyGridItemScope.() -> Unit) {
	item(key) {
		Box(Modifier.animateItem()) {
			content.invoke(this@item)
		}
	}
}

/**
 * Convenience extension function to add a list of items to a [LazyGridScope] that automatically applies the [androidx.compose.foundation.lazy.grid.LazyGridItemScope.animateItem] modifier to each item
 */
fun <T> LazyGridScope.animatedItems(
	items: List<T>,
	key: (item: T) -> Any,
	span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
	contentType: (item: T) -> Any? = { null },
	content: @Composable LazyGridItemScope.(item: T) -> Unit,
) {
	items(items, key, span, contentType) { item ->
		Box(Modifier.animateItem()) {
			content.invoke(this@items, item)
		}
	}
}
