package pl.kwasow.managers

import android.content.Context
import androidx.core.content.edit

class SettingsManagerImpl(
    val context: Context,
) : SettingsManager {
    // ====== Fields
    companion object {
        private const val PREFERENCES_FILE = "pl.kwasow.flamingo.ApplicationSharedPreferences"

        private const val ALLOW_LOCATION_REQUESTS_KEY = "AllowLocationRequests"
        private const val LAST_FCM_TOKEN_SYNC_TIMESTAMP_KEY = "LastFCMTokenSyncTimestamp"
    }

    private val sharedPreferences =
        context.getSharedPreferences(
            PREFERENCES_FILE,
            Context.MODE_PRIVATE,
        )

    override var allowLocationRequests: Boolean
        get() = sharedPreferences.getBoolean(ALLOW_LOCATION_REQUESTS_KEY, true)
        set(value) {
            sharedPreferences.edit {
                putBoolean(ALLOW_LOCATION_REQUESTS_KEY, value)
                apply()
            }
        }

    override var lastFCMTokenSyncTimestamp: Long
        get() = sharedPreferences.getLong(LAST_FCM_TOKEN_SYNC_TIMESTAMP_KEY, 0L)
        set(value) {
            sharedPreferences.edit {
                putLong(LAST_FCM_TOKEN_SYNC_TIMESTAMP_KEY, value)
                apply()
            }
        }
}
