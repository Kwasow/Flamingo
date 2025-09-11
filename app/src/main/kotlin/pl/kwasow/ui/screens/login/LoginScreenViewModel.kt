package pl.kwasow.ui.screens.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.kwasow.managers.UserManager
import pl.kwasow.managers.UserManager.LoginContext

class LoginScreenViewModel(
    private val applicationContext: Context,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    var error by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    // ====== Public methods
    fun launchLogin(onSuccess: () -> Unit) {
        isLoading = true

        val loginContext =
            LoginContext(
                context = applicationContext,
                coroutineScope = viewModelScope,
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onError = {
                    isLoading = false
                    error = it
                },
            )

        userManager.launchGoogleLogin(loginContext)
    }

    fun clearError() {
        error = ""
    }
}
