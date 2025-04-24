package pl.kwasow.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import pl.kwasow.managers.IntentManager
import pl.kwasow.managers.UserManager

// [IMPORTANT]
//
// This is not a global view model! Don't use it as such! It is a view model for the
// App composable and shouldn't be used in any other case.
class AppViewModel(
    private val intentManager: IntentManager,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    val missingYouUrl = intentManager.getMissingYouUrl()

    // ====== Public methods
    fun getInitialRoute(): Any {
        return if (userManager.isUserLoggedIn()) {
            HomeScreen
        } else {
            LoginScreen
        }
    }

    fun runStartupTasks() {
        intentManager.setupShortcuts()
    }
}
