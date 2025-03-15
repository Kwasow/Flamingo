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
}
