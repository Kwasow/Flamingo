package pl.kwasow.managers

interface SettingsManager {
    // ====== Fields
    var allowLocationRequests: Boolean

    var lastFCMTokenSyncTimestamp: Long
}
