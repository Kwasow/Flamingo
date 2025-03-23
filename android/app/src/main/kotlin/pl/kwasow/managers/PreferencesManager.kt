package pl.kwasow.managers

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    // ====== Fields
    val allowLocationRequests: Flow<Boolean>

    var lastFCMTokenSyncTimestamp: Long

    // ====== Methods
    suspend fun setAllowLocationRequests(value: Boolean)
}
