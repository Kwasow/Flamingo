package pl.kwasow.ui.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// ====== Public composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionNotificationView(content: @Composable (granted: Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        content(true)
    } else {
        val permissionState =
            rememberMultiplePermissionsState(
                listOf(android.Manifest.permission.POST_NOTIFICATIONS),
            )

        PermissionView(
            permissionState = permissionState,
            content = content,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionLocationView(content: @Composable (granted: Boolean) -> Unit) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
        )

    PermissionView(
        permissionState = permissionState,
        content = content,
    )
}

// ====== Private composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionView(
    permissionState: MultiplePermissionsState,
    content: @Composable (granted: Boolean) -> Unit,
) {
    LaunchedEffect(true) {
        permissionState.launchMultiplePermissionRequest()
    }

    content(permissionState.allPermissionsGranted)
}
