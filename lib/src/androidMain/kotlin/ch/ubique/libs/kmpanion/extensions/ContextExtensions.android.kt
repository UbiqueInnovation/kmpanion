@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.util.TypedValueCompat
import ch.ubique.libs.kmpanion.permissions.LocationPermissionState

inline fun Context.dpToPx(value: Number): Float {
	return TypedValueCompat.dpToPx(value.toFloat(), resources.displayMetrics)
}

inline fun Context.spToPx(value: Number): Float {
	return TypedValueCompat.spToPx(value.toFloat(), resources.displayMetrics)
}

@SuppressLint("DiscouragedApi")
@IdRes
inline fun Context.getIdRes(identifier: String): Int {
	return resources.getIdentifier(identifier, "id", packageName)
}

@SuppressLint("DiscouragedApi")
@StringRes
inline fun Context.getStringRes(identifier: String): Int {
	return resources.getIdentifier(identifier, "string", packageName)
}

@SuppressLint("DiscouragedApi")
@DrawableRes
inline fun Context.getDrawableRes(identifier: String): Int {
	return resources.getIdentifier(identifier, "drawable", packageName)
}

@JvmOverloads
inline fun Context.getStringByResName(identifier: String, defaultValue: String? = null): String? {
	val res = getStringRes(identifier)
	return if (res != 0) getString(res) else defaultValue
}

inline fun Context.getDrawableByResName(identifier: String): Drawable? {
	val res = getDrawableRes(identifier)
	return if (res != 0) AppCompatResources.getDrawable(this, res) else null
}

/**
 * Check whether the device is a tablet, i.e. its smallest width being >= 600dp.
 */
@JvmOverloads
inline fun Context.isTablet(smallestScreenWidthDpThreshold: Int = 600): Boolean {
	return resources.configuration.smallestScreenWidthDp >= smallestScreenWidthDpThreshold
}

/**
 * Check whether the device's orientation is in portrait mode.
 */
inline fun Context.isPortrait(): Boolean {
	return getOrientation() == Configuration.ORIENTATION_PORTRAIT
}

/**
 * Check whether the device's orientation is in landscape mode.
 */
inline fun Context.isLandscape(): Boolean {
	return getOrientation() == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * Get the device's orientation.
 * May be {@link Configuration#ORIENTATION_LANDSCAPE} or {@link Configuration#ORIENTATION_PORTRAIT}.
 */
inline fun Context.getOrientation(): Int {
	return resources.configuration.orientation
}

/**
 * Check whether a given package is installed on the device.
 * @param packageName package to be checked.
 */
fun Context.isPackageInstalled(packageName: String): Boolean {
	val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return false
	val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong()))
	} else {
		@Suppress("DEPRECATION")
		packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
	}
	return list.isNotEmpty()
}

/**
 * Check whether an accessibility service (Talkback) is enabled.
 */
fun Context.isTalkBackEnabled(): Boolean {
	val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
	return am != null && am.isEnabled && am.isTouchExplorationEnabled
}

/**
 * Check whether night mode (dark mode) is enabled.
 */
fun Context.isInDarkMode(): Boolean {
	val contextNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
	val appCompatNightMode = AppCompatDelegate.getDefaultNightMode()
	return when {
		getActivityContext() != null -> contextNightMode == Configuration.UI_MODE_NIGHT_YES
		appCompatNightMode == AppCompatDelegate.MODE_NIGHT_NO -> false
		appCompatNightMode == AppCompatDelegate.MODE_NIGHT_YES -> true
		else -> contextNightMode == Configuration.UI_MODE_NIGHT_YES
	}
}

/**
 * Get this context or its baseContext as an [Activity].
 * Returns null if it or its baseContext doesn't inherit from [Activity].
 */
fun Context.getActivityContext(): Activity? {
	return when (this) {
		is Activity -> this
		is ContextWrapper -> baseContext.getActivityContext()
		else -> null
	}
}

/**
 * Verifies that this context or its baseContext is of type [Activity] and returns it.
 * Throws an exception if it or its baseContext doesn't inherit from [Activity].
 */
fun Context.requireActivityContext(): Activity {
	return when (this) {
		is Activity -> this
		is ContextWrapper -> baseContext.requireActivityContext()
		else -> throw IllegalStateException("$this has to be an Activity instance.")
	}
}

/**
 * @return True if the [permission] is granted
 */
fun Context.isPermissionGranted(permission: String): Boolean {
	return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * Check whether the device's location services are enabled.
 */
fun Context.isLocationServiceEnabled(): Boolean {
	val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
	return LocationManagerCompat.isLocationEnabled(lm)
}

/**
 * @return returns the current permission state
 */
fun Context.getLocationPermissionState(): LocationPermissionState {
	val isFineGranted = this.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
	val isCoarseGranted = this.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
	val isBackgroundGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
			|| this.isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

	return when {
		isFineGranted && isBackgroundGranted -> LocationPermissionState.FINE_BACKGROUND
		isFineGranted -> LocationPermissionState.FINE_FOREGROUND
		isCoarseGranted && isBackgroundGranted -> LocationPermissionState.COARSE_BACKGROUND
		isCoarseGranted -> LocationPermissionState.COARSE_FOREGROUND
		else -> LocationPermissionState.DENIED
	}
}
