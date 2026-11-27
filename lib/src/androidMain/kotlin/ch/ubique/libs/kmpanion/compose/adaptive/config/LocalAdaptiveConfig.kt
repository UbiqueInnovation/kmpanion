package ch.ubique.libs.kmpanion.compose.adaptive.config

import androidx.compose.runtime.staticCompositionLocalOf

/** Provides the role configuration used by adaptive composables, can be set over the entire app with CompositionLocalProvider */
val LocalAdaptiveRoleConfig = staticCompositionLocalOf { DefaultAdaptiveRoleConfig }
