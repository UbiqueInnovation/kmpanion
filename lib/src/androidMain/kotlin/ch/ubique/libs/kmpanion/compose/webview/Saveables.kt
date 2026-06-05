package ch.ubique.compose.webview

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineScope


/**
 * Creates and remembers a [WebViewNavigator] using the default [CoroutineScope] or a provided
 * override.
 */
@Composable
fun rememberWebViewNavigator(
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
): WebViewNavigator = remember(coroutineScope) { WebViewNavigator(coroutineScope) }

/**
 * Creates a WebView state that is remembered across Compositions.
 *
 * @param url The url to load in the WebView
 * @param additionalHttpHeaders Optional, additional HTTP headers that are passed to [UbiqueWebView.loadUrl].
 *                              Note that these headers are used for all subsequent requests of the WebView.
 */
@Composable
fun rememberWebViewState(
	url: String,
	additionalHttpHeaders: Map<String, String> = emptyMap(),
): WebViewState =
// Rather than using .apply {} here we will recreate the state, this prevents
	// a recomposition loop when the webview updates the url itself.
	remember {
		WebViewState(
			WebContent.Url(
				url = url,
				additionalHttpHeaders = additionalHttpHeaders
			)
		)
	}.apply {
		this.content = WebContent.Url(
			url = url,
			additionalHttpHeaders = additionalHttpHeaders
		)
	}


/**
 * Creates a WebView state that is remembered across Compositions.
 *
 * @param data The uri to load in the WebView
 */
@Composable
fun rememberWebViewStateWithHTMLData(
	data: String,
	baseUrl: String? = null,
	encoding: String = "utf-8",
	mimeType: String? = null,
	historyUrl: String? = null,
): WebViewState {
	//TODO data.replaceFontsWithRegular --> See ETH
	// or data should be already rightly formatted
	val html = data
	return remember {
		WebViewState(WebContent.Data(html, baseUrl, encoding, mimeType, historyUrl))
	}.apply {
		this.content = WebContent.Data(
			html, baseUrl, encoding, mimeType, historyUrl
		)
	}
}


/**
 * Creates a WebView state that is remembered across Compositions.
 *
 * @param url The url to load in the WebView
 * @param postData The data to be posted to the WebView with the url
 */
@Composable
fun rememberWebViewState(
	url: String,
	postData: ByteArray,
): WebViewState =
// Rather than using .apply {} here we will recreate the state, this prevents
	// a recomposition loop when the webview updates the url itself.
	remember {
		WebViewState(
			WebContent.Post(
				url = url,
				postData = postData
			)
		)
	}.apply {
		this.content = WebContent.Post(
			url = url,
			postData = postData
		)
	}


/**
 * Creates a WebView state that is remembered across Compositions and saved
 * across activity recreation.
 * When using saved state, you cannot change the URL via recomposition. The only way to load
 * a URL is via a WebViewNavigator.
 */
@Composable
fun rememberSaveableWebViewState(): WebViewState =
	rememberSaveable(saver = WebStateSaver) {
		WebViewState(WebContent.NavigatorOnly)
	}

val WebStateSaver: Saver<WebViewState, Any> = run {
	val pageTitleKey = "pagetitle"
	val lastLoadedUrlKey = "lastloaded"
	val stateBundle = "bundle"

	mapSaver(
		save = {
			val viewState = Bundle().apply { it.webView?.saveState(this) }
			mapOf(
				pageTitleKey to it.pageTitle,
				lastLoadedUrlKey to it.lastLoadedUrl,
				stateBundle to viewState
			)
		},
		restore = {
			WebViewState(WebContent.NavigatorOnly).apply {
				this.pageTitle = it[pageTitleKey] as String?
				this.lastLoadedUrl = it[lastLoadedUrlKey] as String?
				this.viewState = it[stateBundle] as Bundle?
			}
		}
	)
}