package pl.kwasow.ui.screens.home

import androidx.lifecycle.ViewModel
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

    // ======= Private methods
    private suspend fun checkAuthorization(navigateToLogin: () -> Unit) {
        when (authorizationStatus) {
            Authorization.AUTHORIZED -> return
            Authorization.UNAUTHORIZED -> signOut(navigateToLogin)
            Authorization.UNKNOWN -> {
                val authorization = userManager.checkAuthorization()
                authorizationStatus = authorization

                if (authorization == Authorization.UNAUTHORIZED) {
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
