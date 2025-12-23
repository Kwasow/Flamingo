package pl.kwasow.managers

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.kwasow.BuildConfig
import pl.kwasow.R
import pl.kwasow.flamingo.types.auth.AuthenticationResult
import pl.kwasow.flamingo.types.auth.Authorization
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.managers.UserManager.LoginContext
import pl.kwasow.utils.FlamingoLogger

class UserManagerImpl(
    private val context: Context,
    private val audioManager: AudioManager,
    private val playbackManager: PlaybackManager,
    private val requestManager: RequestManager,
    private val systemManager: SystemManager,
) : UserManager {
    // ====== Fields
    private val firebaseAuth = Firebase.auth
    private val credentialManager = CredentialManager.create(context)

    override val user: MutableLiveData<User?> = MutableLiveData(null)
    override val userFlow: Flow<User?> = user.asFlow()

    // ====== Public methods
    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override suspend fun checkAuthorization(): Authorization = getAuthenticatedUser().authorization

    override suspend fun refreshUser() {
        getAuthenticatedUser()
    }

    override fun launchGoogleLogin(loginContext: LoginContext) = launchGoogleSignIn(loginContext)

    override fun signOut() {
        firebaseAuth.signOut()
        playbackManager.stop()
        audioManager.removeAllDownloads()
        systemManager.clearCache()
    }

    // ====== Private methods
    private suspend fun getAuthenticatedUser(): AuthenticationResult {
        val authenticatedUser = requestManager.getAuthenticatedUser()
        val storedUser = systemManager.getCachedUser()
        val returnedUser = authenticatedUser.authenticatedUser ?: storedUser ?: userFlow.first()

        systemManager.cacheUser(returnedUser)
        user.postValue(returnedUser)

        return AuthenticationResult(
            authenticatedUser.authorization,
            returnedUser,
        )
    }

    private fun launchGoogleSignIn(loginContext: LoginContext) {
        launchGoogleLoginShared(loginContext, true) {
            launchGoogleSignUp(loginContext)
        }
    }

    private fun launchGoogleSignUp(loginContext: LoginContext) {
        launchGoogleLoginShared(loginContext, false) {
            loginContext.onError(context.getString(R.string.login_error))
        }
    }

    private fun launchGoogleLoginShared(
        loginContext: LoginContext,
        filterAuthorizedAccounts: Boolean,
        onFailed: () -> Unit,
    ) {
        val googleIdOption =
            GetGoogleIdOption
                .Builder()
                .setFilterByAuthorizedAccounts(filterAuthorizedAccounts)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .build()

        val request =
            GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build()

        loginContext.coroutineScope.launch {
            try {
                val result =
                    credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                handleSignIn(result, loginContext)
            } catch (e: GetCredentialException) {
                FlamingoLogger.e(
                    "Google login failed - filterByAuthorizedAccounts: " +
                        "$filterAuthorizedAccounts - ($e)",
                )
                onFailed()
            }
        }
    }

    private fun handleSignIn(
        result: GetCredentialResponse,
        loginContext: LoginContext,
    ) {
        val credential = result.credential

        val onUnknownCredential = {
            FlamingoLogger.e("Unknown credential type")
            loginContext.onError(context.getString(R.string.login_error))
        }

        when (credential) {
            is CustomCredential -> {
                if (isGoogleCredential(credential.type)) {
                    handleGoogleCredential(credential, loginContext)
                } else {
                    onUnknownCredential()
                }
            }
            else -> {
                onUnknownCredential()
            }
        }
    }

    private fun isGoogleCredential(type: String): Boolean =
        type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

    private fun handleGoogleCredential(
        credential: Credential,
        loginContext: LoginContext,
    ) {
        val googleIdTokenCredential =
            GoogleIdTokenCredential
                .createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth
            .signInWithCredential(firebaseCredential)
            .addOnCompleteListener { verifyFirebaseLogin(it, loginContext) }
    }

    private fun verifyFirebaseLogin(
        task: Task<AuthResult>,
        loginContext: LoginContext,
    ) {
        loginContext.coroutineScope.launch {
            if (task.isSuccessful) {
                val authResult = requestManager.getAuthenticatedUser()

                when (authResult.authorization) {
                    Authorization.AUTHORIZED -> {
                        FlamingoLogger.i("Google and Firebase login success")
                        loginContext.onSuccess()
                    }
                    Authorization.UNAUTHORIZED -> {
                        FlamingoLogger.e("Firebase login failed - access not granted")
                        loginContext.onError(context.getString(R.string.login_error_no_access))
                        signOut()
                    }
                    Authorization.UNKNOWN -> {
                        FlamingoLogger.e("Firebase login failed - couldn't connect to server")
                        loginContext.onError(context.getString(R.string.login_error_no_connection))
                        signOut()
                    }
                }
            } else {
                FlamingoLogger.e("Firebase login failed (${task.exception})")
                loginContext.onError(context.getString(R.string.login_error))
            }
        }
    }
}
