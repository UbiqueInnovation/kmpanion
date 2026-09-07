package ch.ubique.libs.kmpanion.lifecycle

import kotlinx.coroutines.flow.Flow

/**
 * A platform-specific source providing the application lifecycle
 */
interface AppLifecycleSource {
	val state: Flow<AppLifecycleState>
}
