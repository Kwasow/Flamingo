package pl.kwasow.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import pl.kwasow.data.types.AuthenticationResult
import pl.kwasow.managers.MessagingManager
import pl.kwasow.managers.PermissionManager
import pl.kwasow.managers.UserManager

class HomeScreenViewModel(
    private val messagingManager: MessagingManager,
    private val permissionManager: PermissionManager,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    private var authorizationStatus = AuthenticationResult.Authorization.UNKNOWN

    // ====== Public methods
    suspend fun doLaunchTasks(navigateToLogin: () -> Unit) {
        if (authorizationStatus == AuthenticationResult.Authorization.AUTHORIZED) {
            return
        }

        // Check if user is authenticated
        val authenticationResult = userManager.getAuthenticatedUser()
        authorizationStatus = authenticationResult.authorization

        if (authenticationResult.authorization == AuthenticationResult.Authorization.UNAUTHORIZED) {
            userManager.signOut()
            navigateToLogin()
        } else {
            // These tasks require the user to be logged in
            messagingManager.sendFcmToken()
            messagingManager.subscribeToTopics()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @ExperimentalPermissionsApi
    @Composable
    fun rememberNotificationPermissionState(): PermissionState =
        permissionManager.rememberNotificationPermissionState()
}
