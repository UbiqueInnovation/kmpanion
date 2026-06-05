package ch.ubique.libs.kmpanion.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import ch.ubique.libs.kmpanion.extensions.isPermissionGranted

abstract class RuntimePermissionHandler protected constructor(
	private val permissions: Set<String>,
	private val listener: Listener,
) {

	protected abstract val launcher: ActivityResultLauncher<Array<String>>

	/**
	 * @return True if all the permissions this handler is configured for are granted
	 */
	fun isPermissionGranted() = permissions.all { requireContext().isPermissionGranted(it) }

	/**
	 * Should be called when the user clicks the "Grant permission" button
	 */
	fun requestPermission() {
		when {
			isPermissionGranted() -> {
				// If the permission is already granted, immediately call the listener
				listener.onPermissionResult(true)
			}
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && permissions.any { it == Manifest.permission.ACCESS_FINE_LOCATION } -> {
				// Special case ACCESS_FINE_LOCATION also needs ACCESS_COARSE_LOCATION
				val permissionsWithCoarse = permissions + Manifest.permission.ACCESS_COARSE_LOCATION
				launcher.launch(permissionsWithCoarse.toTypedArray())
			}
			Build.VERSION.SDK_INT >= getPermissionIntroducedInSdkVersion() -> {
				// Make sure the permission exists on the current SDK version, since the launcher will do nothing if it doesn't
				launcher.launch(permissions.toTypedArray())
			}
			else -> {
				// Permission does not yet exist on this SDK version but is denied due to some pre-existing setting (e.g. Notification permission)
				val intent = createPermissionSpecificAppSettingsIntent()
				listener.showJumpToAppSettingsExplanation(intent)
			}
		}
	}

	/**
	 * Open the correct settings screen for the permissions this handler is configured for
	 */
	fun openAppSettings() {
		val intent = createPermissionSpecificAppSettingsIntent()
		requireContext().startActivity(intent)
	}

	/**
	 * @return An intent that opens the correct settings screen for the permissions this handler is configured for
	 */
	fun createPermissionSpecificAppSettingsIntent(): Intent {
		return if (permissions.singleOrNull() == Manifest.permission.POST_NOTIFICATIONS && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
				.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
		} else {
			Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				.addCategory(Intent.CATEGORY_DEFAULT)
				.setData(Uri.parse("package:${requireContext().packageName}"))
		}
	}

	protected abstract fun requireContext(): Context
	protected abstract fun shouldShowRequestPermissionRationale(permission: String): Boolean

	protected fun handlePermissionResult(isGranted: Boolean) {
		listener.onPermissionResult(isGranted)
		if (!isGranted) {
			val isAllowedToRequestPermissionAgain = permissions.any { shouldShowRequestPermissionRationale(it) }

			if (isAllowedToRequestPermissionAgain) {
				// Do nothing, the user can request the permission again and the system dialog will appear one more time
			} else {
				// If the show rationale flag is false (after cancelling the first time or after denying twice), show a rationale to
				// the user, that will lead to the settings
				val intent = createPermissionSpecificAppSettingsIntent()
				listener.showJumpToAppSettingsExplanation(intent)
			}
		}
	}

	private fun getPermissionIntroducedInSdkVersion(): Int {
		return when {
			// New in Android 13
			permissions.any {
				it in setOf(
					Manifest.permission.READ_MEDIA_AUDIO,
					Manifest.permission.READ_MEDIA_IMAGES,
					Manifest.permission.READ_MEDIA_VIDEO,
					Manifest.permission.POST_NOTIFICATIONS
				)
			} -> Build.VERSION_CODES.TIRAMISU

			// New in Android 12
			permissions.any {
				it in setOf(
					Manifest.permission.BLUETOOTH_SCAN,
					Manifest.permission.BLUETOOTH_ADVERTISE,
					Manifest.permission.BLUETOOTH_CONNECT,
				)
			} -> Build.VERSION_CODES.S

			// All others
			else -> Build.VERSION_CODES.BASE
		}
	}

	interface Listener {
		/**
		 * Callback invoked by a permission request with its result
		 * @param isGranted True if the permission is granted, false if the permission is denied or the request was cancelled
		 */
		fun onPermissionResult(isGranted: Boolean)

		/**
		 * Callback invoked when the handler has failed to resolve a permission request and the app should now show an explanation
		 * for the user to jump into the app settings to fix the missing permission.
		 * @param intent The correct intent to start depending on the permission this handler is configured for
		 */
		fun showJumpToAppSettingsExplanation(intent: Intent)
	}

}

private class ActivityPermissionHandler(
	private val activity: ComponentActivity,
	permissions: Set<String>,
	listener: Listener,
) : RuntimePermissionHandler(permissions, listener) {

	override val launcher =
		activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
			handlePermissionResult(grants.all { it.value })
		}

	override fun requireContext(): Context {
		return activity
	}

	override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
		return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
	}
}

private class FragmentPermissionHandler(
	private val fragment: Fragment,
	permissions: Set<String>,
	listener: Listener,
) : RuntimePermissionHandler(permissions, listener) {

	override val launcher =
		fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
			handlePermissionResult(grants.all { it.value })
		}

	override fun requireContext(): Context {
		return fragment.requireContext()
	}

	override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
		return fragment.shouldShowRequestPermissionRationale(permission)
	}
}

fun ComponentActivity.registerPermissionHandler(
	permission: String,
	listener: RuntimePermissionHandler.Listener,
): RuntimePermissionHandler = ActivityPermissionHandler(this, setOf(permission), listener)

fun ComponentActivity.registerPermissionHandler(
	permissions: Set<String>,
	listener: RuntimePermissionHandler.Listener,
): RuntimePermissionHandler = ActivityPermissionHandler(this, permissions, listener)

fun Fragment.registerPermissionHandler(
	permission: String,
	listener: RuntimePermissionHandler.Listener,
): RuntimePermissionHandler = FragmentPermissionHandler(this, setOf(permission), listener)

fun Fragment.registerPermissionHandler(
	permissions: Set<String>,
	listener: RuntimePermissionHandler.Listener,
): RuntimePermissionHandler = FragmentPermissionHandler(this, permissions, listener)