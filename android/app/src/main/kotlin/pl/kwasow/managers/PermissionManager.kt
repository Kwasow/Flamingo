package pl.kwasow.managers

import android.app.Activity

interface PermissionManager {
    // ====== Public methods
    fun launchPermissionSettings(activity: Activity)

    fun checkBackgroundLocationPermission(): Boolean
}
