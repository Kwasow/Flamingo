package pl.kwasow.services

import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import org.koin.android.ext.android.inject
import pl.kwasow.managers.FlamingoDownloadManager

class PlaybackService : MediaSessionService() {
    // ====== Fields
    private var mediaSession: MediaSession? = null

    private val flamingoDownloadManager by inject<FlamingoDownloadManager>()

    // ====== Interface methods
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val cacheDataSourceFactory =
            CacheDataSource
                .Factory()
                .setCache(flamingoDownloadManager.downloadCache)
                .setUpstreamDataSourceFactory(flamingoDownloadManager.sourceFactory)
                .setCacheWriteDataSinkFactory(null)
        val player =
            ExoPlayer
                .Builder(this)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(this).setDataSourceFactory(cacheDataSourceFactory),
                ).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }

        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession
}
