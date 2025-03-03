package pl.kwasow.sunshine.services

import android.app.Notification
import android.content.Context
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import org.koin.android.ext.android.inject
import pl.kwasow.sunshine.R
import pl.kwasow.sunshine.managers.NotificationManager
import pl.kwasow.sunshine.utils.DownloadUtils

@UnstableApi
class SunshineDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    null,
    -1,
    -1,
) {
    // ====== Fields
    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1

        private class TerminalStateNotificationHelper(
            context: Context,
            private val notificationHelper: DownloadNotificationHelper,
            private var nextNotificationId: Int,
        ) : DownloadManager.Listener {
            private val context: Context = context.applicationContext

            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?,
            ) {
                val notification =
                    when (download.state) {
                        Download.STATE_COMPLETED -> {
                            notificationHelper.buildDownloadCompletedNotification(
                                context,
                                R.drawable.ic_download_done,
                                null,
                                Util.fromUtf8Bytes(download.request.data),
                            )
                        }

                        Download.STATE_FAILED -> {
                            notificationHelper.buildDownloadFailedNotification(
                                context,
                                R.drawable.ic_download_done,
                                null,
                                Util.fromUtf8Bytes(download.request.data),
                            )
                        }

                        else -> return
                    }
                NotificationUtil.setNotification(context, nextNotificationId++, notification)
            }
        }
    }

    private val notificationManager by inject<NotificationManager>()

    // ====== Interface methods
    override fun getDownloadManager(): DownloadManager {
        // This will only happen once, because getDownloadManager is guaranteed to be called only once
        // in the life cycle of the process.
        val downloadManager = DownloadUtils.getDownloadManager(this)
        val helper = DownloadUtils.getDownloadNotificationHelper(this, notificationManager.downloadChannelInfo.channelId)
        downloadManager.addListener(
            TerminalStateNotificationHelper(this, helper, FOREGROUND_NOTIFICATION_ID + 1),
        )

        return downloadManager
    }

    override fun getScheduler(): Scheduler = PlatformScheduler(this, JOB_ID)

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int,
    ): Notification {
        return DownloadUtils.getDownloadNotificationHelper(this, notificationManager.downloadChannelInfo.channelId)
            .buildProgressNotification(
                this,
                R.drawable.ic_download,
                null,
                null,
                downloads,
                notMetRequirements,
            )
    }
}
