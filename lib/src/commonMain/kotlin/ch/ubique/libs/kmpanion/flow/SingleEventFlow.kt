package ch.ubique.libs.kmpanion.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Flow equivalent of the SingleLiveEvent LiveData implementation that ensures an emitted value is collected exactly once.
 * This implementation uses a buffered channel that is received as a flow, as explained in this article:
 * https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
 */
class SingleEventFlow<T> {

	private val bufferedChannel = Channel<T>(Channel.BUFFERED)

	var hasFiredAtLeastOnce = false
		private set

	fun emit(value: T) {
		bufferedChannel.trySend(value)
		hasFiredAtLeastOnce = true
	}

	fun asFlow() = bufferedChannel.receiveAsFlow()

}

/**
 * Used for cases where T is Unit, to make calls cleaner.
 */
fun SingleEventFlow<Unit>.emit() {
	emit(Unit)
}
