package pl.kwasow.managers

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kwasow.data.store.userPreferencesDataStore

class PreferencesManagerImpl(
    val context: Context,
) : PreferencesManager {
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

    override val allowLocationRequests: Flow<Boolean> =
        context.userPreferencesDataStore.data
            .map { preferences ->
                preferences.allowLocationRequests
            }

    override var lastFCMTokenSyncTimestamp: Long
        get() = sharedPreferences.getLong(LAST_FCM_TOKEN_SYNC_TIMESTAMP_KEY, 0L)
        set(value) {
            sharedPreferences.edit {
                putLong(LAST_FCM_TOKEN_SYNC_TIMESTAMP_KEY, value)
                apply()
            }
        }

    // ====== Public methods
    override suspend fun setAllowLocationRequests(value: Boolean) {
        context.userPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setAllowLocationRequests(value)
                .build()
        }
    }
}
