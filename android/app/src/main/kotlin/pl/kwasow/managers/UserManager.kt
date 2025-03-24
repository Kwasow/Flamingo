package pl.kwasow.managers

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import pl.kwasow.data.types.AuthenticationResult
import pl.kwasow.data.types.User

interface UserManager {
    // ====== Classes
    data class LoginContext(
        val context: Context,
        val coroutineScope: CoroutineScope,
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit,
    )

    // ====== Fields
    val user: LiveData<User?>
    val userFlow: Flow<User?>

    // ====== Methods
    fun isUserLoggedIn(): Boolean

    suspend fun checkAuthorization(): AuthenticationResult.Authorization

    suspend fun refreshUser()

    fun launchGoogleLogin(loginContext: LoginContext)

    fun signOut()
}
