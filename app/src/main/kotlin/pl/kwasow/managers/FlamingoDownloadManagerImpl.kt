package pl.kwasow.managers

import android.content.Context
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class FlamingoDownloadManagerImpl(
    private val context: Context,
    private val notificationManager: NotificationManager,
) : FlamingoDownloadManager {
    // ====== Fields
    companion object {
        private const val DOWNLOADS_DIR = "audio_downloads"
    }

    override val downloadNotificationHelper by lazy {
        DownloadNotificationHelper(context, notificationManager.downloadChannelInfo.channelId)
    }

    override val downloadManager by lazy {
        DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            sourceFactory,
            Executors.newFixedThreadPool(4),
        )
    }

    override val downloadCache: Cache by lazy {
        SimpleCache(
            File(context.filesDir, DOWNLOADS_DIR),
            NoOpCacheEvictor(),
            databaseProvider,
        )
    }

    override val sourceFactory by lazy {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        CookieHandler.setDefault(cookieManager)
        DefaultHttpDataSource.Factory()
    }

    private val databaseProvider: DatabaseProvider by lazy {
        StandaloneDatabaseProvider(context)
    }
}
