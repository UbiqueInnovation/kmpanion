package ch.ubique.libs.kmpanion.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class AndroidAppLifecycleSource : AppLifecycleSource {
	override val state: Flow<AppLifecycleState> = callbackFlow {
		val observer = object : DefaultLifecycleObserver {
			override fun onStart(owner: LifecycleOwner) {
				trySend(AppLifecycleState.FOREGROUND)
			}

			override fun onStop(owner: LifecycleOwner) {
				trySend(AppLifecycleState.BACKGROUND)
			}
		}

		val lifecycle = ProcessLifecycleOwner.get().lifecycle
		if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
			trySend(AppLifecycleState.FOREGROUND)
		}

		lifecycle.addObserver(observer)
		awaitClose { lifecycle.removeObserver(observer) }
	}.flowOn(Dispatchers.Main.immediate)
}
