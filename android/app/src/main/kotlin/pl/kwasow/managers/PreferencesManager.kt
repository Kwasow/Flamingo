package pl.kwasow.managers

interface PreferencesManager {
    // ====== Fields
    var allowLocationRequests: Boolean

    var lastFCMTokenSyncTimestamp: Long
}
