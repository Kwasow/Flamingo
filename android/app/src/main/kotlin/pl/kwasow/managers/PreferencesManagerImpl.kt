package pl.kwasow.managers

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kwasow.data.store.appStateDataStore
import pl.kwasow.data.store.userPreferencesDataStore

class PreferencesManagerImpl(
    val context: Context,
) : PreferencesManager {
    // ====== Fields
    override val allowLocationRequests: Flow<Boolean> =
        context.userPreferencesDataStore.data
            .map { preferences ->
                preferences.allowLocationRequests
            }

    override var lastFcmTokenSyncTimestamp: Flow<Long> =
        context.appStateDataStore.data
            .map { preferences ->
                preferences.lastFcmTokenSync
            }

    // ====== Public methods
    override suspend fun setAllowLocationRequests(value: Boolean) {
        context.userPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setAllowLocationRequests(value)
                .build()
        }
    }

    override suspend fun setLastFcmTokenSyncTimestamp(value: Long) {
        context.appStateDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setLastFcmTokenSync(value)
                .build()
        }
    }
}
