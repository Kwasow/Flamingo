package pl.kwasow.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.kwasow.R
import pl.kwasow.managers.PermissionManager
import pl.kwasow.managers.PreferencesManager
import pl.kwasow.managers.SystemManager
import pl.kwasow.managers.UserManager

class SettingsScreenViewModel(
    private val applicationContext: Context,
    private val permissionManager: PermissionManager,
    private val preferencesManager: PreferencesManager,
    private val systemManager: SystemManager,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    var allowLocationRequests =
        MutableLiveData(
            preferencesManager.allowLocationRequests &&
                permissionManager.checkBackgroundLocationPermission(),
        )
        private set

    val partnerName =
        userManager.getCachedUser()?.missingYouRecipient?.firstName
            ?: applicationContext.getString(R.string.partner)

    // ====== Methods
    fun freeUpMemory() {
        systemManager.clearCoilCache()
        Toast.makeText(
            applicationContext,
            applicationContext.getString(R.string.settings_cache_cleared),
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun signOut(onSuccess: () -> Unit) {
        userManager.signOut()
        onSuccess()
    }

    fun launchStore() = systemManager.launchStore()

    fun launchPermissionSettings(activity: Activity) =
        permissionManager.launchPermissionSettings(activity)

    fun toggleAllowLocationRequests(onPermissionMissing: () -> Unit) {
        if (!permissionManager.checkBackgroundLocationPermission()) {
            onPermissionMissing()
        } else {
            val newValue = allowLocationRequests.value != true
            preferencesManager.allowLocationRequests = newValue
            allowLocationRequests.value = newValue
        }
    }

    fun updateAllowLocationRequestState(wasPermissionMissing: Boolean) {
        if (wasPermissionMissing) {
            val newValue = permissionManager.checkBackgroundLocationPermission()

            allowLocationRequests.value = newValue
            preferencesManager.allowLocationRequests = newValue
        }
    }
}
