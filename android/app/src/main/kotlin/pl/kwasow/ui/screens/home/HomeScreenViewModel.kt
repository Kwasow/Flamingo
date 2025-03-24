package pl.kwasow.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import pl.kwasow.data.types.AuthenticationResult.Authorization
import pl.kwasow.managers.MessagingManager
import pl.kwasow.managers.PermissionManager
import pl.kwasow.managers.UserManager

class HomeScreenViewModel(
    private val messagingManager: MessagingManager,
    private val permissionManager: PermissionManager,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    private var authorizationStatus = Authorization.UNKNOWN

    // ====== Public methods
    suspend fun doLaunchTasks(navigateToLogin: () -> Unit) {
        checkAuthorization(navigateToLogin)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @ExperimentalPermissionsApi
    @Composable
    fun rememberNotificationPermissionState(): PermissionState =
        permissionManager.rememberNotificationPermissionState()

    // ======= Private methods
    private suspend fun checkAuthorization(navigateToLogin: () -> Unit) {
        when (authorizationStatus) {
            Authorization.AUTHORIZED -> return
            Authorization.UNAUTHORIZED -> signOut(navigateToLogin)
            Authorization.UNKNOWN -> {
                val authenticationResult = userManager.getAuthenticatedUser()
                authorizationStatus = authenticationResult.authorization

                if (authenticationResult.authorization == Authorization.UNAUTHORIZED) {
                    signOut(navigateToLogin)
                } else {
                    messagingManager.sendFcmToken()
                    messagingManager.subscribeToTopics()
                }
            }
        }
    }

    private fun signOut(navigateToLogin: () -> Unit) {
        userManager.signOut()
        navigateToLogin()
    }
}
