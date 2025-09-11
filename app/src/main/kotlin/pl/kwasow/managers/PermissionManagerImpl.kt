package pl.kwasow.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

class PermissionManagerImpl(
    private val context: Context,
) : PermissionManager {
    // ====== Public methods
    override fun launchPermissionSettings(activity: Activity) {
        val intent =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                ("package:" + context.packageName).toUri(),
            )

        activity.startActivity(intent)
    }

    override fun checkBackgroundLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return true
        }

        val res =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )

        return res == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}
