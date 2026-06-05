package ch.ubique.libs.kmpanion.lifecycle

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationState

@OptIn(ExperimentalForeignApi::class)
private class IosAppLifecycleSource : AppLifecycleSource {
	override val state: Flow<AppLifecycleState> = callbackFlow {
		val notificationCenter = NSNotificationCenter.defaultCenter
		val mainQueue = NSOperationQueue.mainQueue

		val initialState = if (UIApplication.sharedApplication.applicationState == UIApplicationState.UIApplicationStateActive) {
			AppLifecycleState.FOREGROUND
		} else {
			AppLifecycleState.BACKGROUND
		}
		trySend(initialState)

		val activeObserver = notificationCenter.addObserverForName(
			name = UIApplicationDidBecomeActiveNotification,
			`object` = null,
			queue = mainQueue,
		) { _ -> trySend(AppLifecycleState.FOREGROUND) }

		val backgroundObserver = notificationCenter.addObserverForName(
			name = UIApplicationDidEnterBackgroundNotification,
			`object` = null,
			queue = mainQueue,
		) { _ -> trySend(AppLifecycleState.BACKGROUND) }

		awaitClose {
			notificationCenter.removeObserver(activeObserver)
			notificationCenter.removeObserver(backgroundObserver)
		}
	}.flowOn(Dispatchers.Main)
}
