package ch.ubique.compose.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class WebViewState(webContent: WebContent) {
	var lastLoadedUrl: String? by mutableStateOf(null)
		internal set

	/**
	 *  The content being loaded by the WebView
	 */
	var content: WebContent by mutableStateOf(webContent)

	/**
	 * Whether the WebView is currently [LoadingState.Loading] data in its main frame (along with
	 * progress) or the data loading has [LoadingState.Finished]. See [LoadingState]
	 */
	var loadingState: LoadingState by mutableStateOf(LoadingState.Initializing)
		internal set

	/**
	 * Whether the webview is currently loading data in its main frame
	 */
	val isLoading: Boolean
		get() = loadingState !is LoadingState.Finished

	/**
	 * The title received from the loaded content of the current page
	 */
	var pageTitle: String? by mutableStateOf(null)
		internal set

	/**
	 * the favicon received from the loaded content of the current page
	 */
	var pageIcon: Bitmap? by mutableStateOf(null)
		internal set

	/**
	 * A list for errors captured in the last load. Reset when a new page is loaded.
	 * Errors could be from any resource (iframe, image, etc.), not just for the main page.
	 * For more fine grained control use the OnError callback of the WebView.
	 */
	val errorsForCurrentRequest: SnapshotStateList<WebViewError> = mutableStateListOf()

	/**
	 * The saved view state from when the view was destroyed last. To restore state,
	 * use the navigator and only call loadUrl if the bundle is null.
	 * See WebViewSaveStateSample.
	 */
	var viewState: Bundle? = null
		internal set

	// We need access to this in the state saver. An internal DisposableEffect or AndroidView
	// onDestroy is called after the state saver and so can't be used.
	internal var webView by mutableStateOf<WebView?>(null)
}