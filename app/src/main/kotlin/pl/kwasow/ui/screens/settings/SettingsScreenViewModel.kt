package pl.kwasow.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    val allowLocationRequests = preferencesManager.allowLocationRequests

    val partnerName =
        userManager.user.value?.partner?.firstName
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
        viewModelScope.launch {
            if (!permissionManager.checkBackgroundLocationPermission()) {
                onPermissionMissing()
            } else {
                preferencesManager.setAllowLocationRequests(!allowLocationRequests.first())
            }
        }
    }

    fun updateAllowLocationRequestState(wasPermissionMissing: Boolean) {
        viewModelScope.launch {
            if (wasPermissionMissing) {
                preferencesManager.setAllowLocationRequests(
                    permissionManager.checkBackgroundLocationPermission(),
                )
            }
        }
    }
}
