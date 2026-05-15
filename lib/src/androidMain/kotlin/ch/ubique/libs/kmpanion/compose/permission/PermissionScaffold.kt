package ch.ubique.libs.kmpanion.compose.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PermissionScaffold(
	permission: String,
	permissionRationaleContent: @Composable (settingsIntent: Intent, onHandled: () -> Unit) -> Unit,
	permissionNotGrantedContent: @Composable (handler: PermissionHandler) -> Unit,
	permissionGrantedContent: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	requestImmediately: Boolean = false,
	onPermissionGranted: () -> Unit,
) {
	val permissionList = when {
		permission == Manifest.permission.ACCESS_FINE_LOCATION && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			persistentListOf(permission, Manifest.permission.ACCESS_COARSE_LOCATION)
		}
		else -> persistentListOf(permission)
	}

	MultiPermissionScaffold(
		modifier = modifier,
		permissions = permissionList,
		requestImmediately = requestImmediately,
		permissionRationaleContent = permissionRationaleContent,
		permissionNotGrantedContent = permissionNotGrantedContent,
		permissionGrantedContent = permissionGrantedContent,
		onPermissionGranted = onPermissionGranted,
	)
}

@SuppressLint("ComposeModifierReused")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiPermissionScaffold(
	permissions: ImmutableList<String>,
	modifier: Modifier = Modifier,
	requestImmediately: Boolean = false,
	permissionRationaleContent: @Composable (settingsIntent: Intent, onHandled: () -> Unit) -> Unit = { _, _ -> },
	permissionNotGrantedContent: @Composable (handler: PermissionHandler) -> Unit = {},
	permissionGrantedContent: @Composable () -> Unit = {},
	onPermissionGranted: () -> Unit = {},
) {
	if (LocalInspectionMode.current) {
		// Treat permission as granted for previews
		Box(modifier = modifier) {
			permissionGrantedContent.invoke()
		}
		return
	}

	val context = LocalContext.current
	var showPermissionRationale by rememberSaveable { mutableStateOf(false) }
	var shouldRequestPermission by rememberSaveable { mutableStateOf(false) }
	var wasDenied by rememberSaveable { mutableStateOf(false) }

	val permissionState = rememberMultiplePermissionsState(permissions) { grants ->
		val isGranted = grants.all { (_, isGranted) -> isGranted }
		if (isGranted) {
			wasDenied = false
			onPermissionGranted.invoke()
		} else {
			wasDenied = true
		}

		showPermissionRationale = false
		shouldRequestPermission = false
	}

	Box(modifier = modifier) {
		// Display the appropriate content based on the permission state
		if (permissionState.allPermissionsGranted) {
			permissionGrantedContent.invoke()
		} else {
			val callback = PermissionHandler {
				shouldRequestPermission = true
			}
			permissionNotGrantedContent.invoke(callback)
		}
	}

	if (showPermissionRationale) {
		// Show the permission rationale content and pass in the intent that leads to the correct app settings screen
		val settingsIntent = createPermissionSpecificSettingsIntent(context, permissions.first())
		permissionRationaleContent.invoke(settingsIntent) {
			showPermissionRationale = false
			wasDenied = false
		}
	}

	LaunchedEffect(wasDenied) {
		if (wasDenied && !permissionState.shouldShowRationale) {
			showPermissionRationale = true
		}
	}

	LaunchedEffect(requestImmediately) {
		// If the permission should be requested immediately (as opposed to a user action in the [permissionNotGrantedContent]), set the flag to request the permission
		if (requestImmediately) {
			shouldRequestPermission = true
		}
	}

	LaunchedEffect(shouldRequestPermission) {
		// Actively request the permission or trigger the rationale if the flag is set
		if (shouldRequestPermission) {
			requestPermissionOrSettingsChange(
				permission = permissions.first(),
				isGranted = permissionState.allPermissionsGranted,
				onRequestPermission = {
					wasDenied = false
					permissionState.launchMultiplePermissionRequest()
				},
				onPermissionGranted = {
					showPermissionRationale = false
				},
				onShowRationale = {
					showPermissionRationale = true
				},
			)

			shouldRequestPermission = false
		}
	}
}

private fun requestPermissionOrSettingsChange(
	permission: String,
	isGranted: Boolean,
	onRequestPermission: () -> Unit,
	onPermissionGranted: () -> Unit,
	onShowRationale: () -> Unit,
) {
	when {
		isGranted -> {
			// Permission is granted, immediately invoke the callback
			onPermissionGranted.invoke()
		}
		permission == Manifest.permission.ACCESS_FINE_LOCATION && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			onRequestPermission.invoke()
		}
		Build.VERSION.SDK_INT < getPermissionIntroducedInSdkVersion(permission) -> {
			// The permission launcher does nothing if the permission was introduced in a higher SDK version, show the rationale to jump to the app settings
			onShowRationale.invoke()
		}
		else -> {
			// Request the permission
			onRequestPermission.invoke()
		}
	}
}

private fun createPermissionSpecificSettingsIntent(context: Context, permission: String): Intent {
	return when {
		permission == Manifest.permission.POST_NOTIFICATIONS -> {
			Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
				.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
		}
		else -> {
			Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				.addCategory(Intent.CATEGORY_DEFAULT)
				.setData("package:${context.packageName}".toUri())
		}
	}
}

private fun getPermissionIntroducedInSdkVersion(permission: String): Int {
	return when (permission) {
		Manifest.permission.ACCESS_BACKGROUND_LOCATION -> Build.VERSION_CODES.Q
		Manifest.permission.BLUETOOTH_ADVERTISE,
		Manifest.permission.BLUETOOTH_CONNECT,
		Manifest.permission.BLUETOOTH_SCAN,
			-> Build.VERSION_CODES.S
		Manifest.permission.READ_MEDIA_AUDIO,
		Manifest.permission.READ_MEDIA_IMAGES,
		Manifest.permission.READ_MEDIA_VIDEO,
		Manifest.permission.POST_NOTIFICATIONS,
			-> Build.VERSION_CODES.TIRAMISU
		else -> Build.VERSION_CODES.BASE
	}
}