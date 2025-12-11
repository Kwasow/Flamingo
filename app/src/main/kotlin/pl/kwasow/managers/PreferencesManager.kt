package pl.kwasow.managers

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    // ====== Fields
    val allowLocationRequests: Flow<Boolean>

    val lastFcmTokenSyncTimestamp: Flow<Long>

    // ====== Methods
    suspend fun setAllowLocationRequests(value: Boolean)

    suspend fun setLastFcmTokenSyncTimestamp(value: Long)
}
