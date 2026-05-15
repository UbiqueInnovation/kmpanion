package ch.ubique.libs.kmpanion.permissions

enum class LocationPermissionState(val isBackground: Boolean, val isFine: Boolean, val isGranted: Boolean) {
	FINE_FOREGROUND(false, true, true),
	COARSE_FOREGROUND(false, false, true),
	FINE_BACKGROUND(true, true, true),
	COARSE_BACKGROUND(true, false, true),
	DENIED(false, false, false)
}