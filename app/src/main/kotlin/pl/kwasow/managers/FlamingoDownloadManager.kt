package pl.kwasow.managers

import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
interface FlamingoDownloadManager {
    // ====== Fields
    val downloadNotificationHelper: DownloadNotificationHelper

    val downloadManager: DownloadManager

    val downloadCache: Cache

    val sourceFactory: DataSource.Factory
}
