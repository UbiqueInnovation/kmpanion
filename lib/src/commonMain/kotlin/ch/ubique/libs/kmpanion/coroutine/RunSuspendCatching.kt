package ch.ubique.libs.kmpanion.coroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

suspend inline fun <T, R> T.runSuspendCatching(block: suspend T.() -> R): Result<R> {
	return try {
		Result.success(block())
	} catch (e: Throwable) {
		if (e is CancellationException) currentCoroutineContext().ensureActive()
		Result.failure(e)
	}
}