package pl.kwasow.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.kwasow.MainActivity
import pl.kwasow.R
import pl.kwasow.data.NotificationChannelInfo
import pl.kwasow.utils.FlamingoLogger
import java.time.Instant
import android.app.NotificationManager as AndroidNotificationManager

class NotificationManagerImpl(
    private val context: Context,
) : NotificationManager {
    // ====== Fields
    override val memoriesChannelInfo =
        NotificationChannelInfo(
            R.string.notificationChannel_memories,
            R.string.notificationChannel_memories_description,
            "memories",
        )
    override val downloadChannelInfo =
        NotificationChannelInfo(
            R.string.notificationChannel_downloads,
            R.string.notificationChannel_downloads_description,
            "downloads",
        )
    override val missingYouChannelInfo =
        NotificationChannelInfo(
            R.string.notificationChannel_missingyou,
            R.string.notificationChannel_missingyou_description,
            "missingyou",
            R.raw.magic,
            NotificationManagerCompat.IMPORTANCE_HIGH,
        )

    // ====== Constructors
    init {
        val notificationManager = context.getSystemService(AndroidNotificationManager::class.java)

        createChannel(notificationManager, memoriesChannelInfo)
        createChannel(notificationManager, downloadChannelInfo)
        createChannel(notificationManager, missingYouChannelInfo)
    }

    // ====== Public methods
    override fun postMemoryNotification() {
        val startAppIntent =
            Intent(Intent.ACTION_MAIN).apply {
                component = ComponentName(context, MainActivity::class.java)
                `package` = context.packageName
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        val startAppPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                startAppIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(context, memoriesChannelInfo.channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notification_memories_title))
                .setContentText(context.getString(R.string.notification_memories_text))
                .setContentIntent(startAppPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build()

        postNotification(notification)
    }

    override fun postMissingYouNotification(senderName: String) {
        val startAppIntent =
            Intent(Intent.ACTION_MAIN).apply {
                component = ComponentName(context, MainActivity::class.java)
                `package` = context.packageName
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        val startAppPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                startAppIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(context, missingYouChannelInfo.channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.module_missingyou_miss_you))
                .setContentText(getRandomMissingYouText(context, senderName))
                .setContentIntent(startAppPendingIntent)
                .setAutoCancel(true)
                .build()

        postNotification(notification)
    }

    // ====== Private methods
    private fun createChannel(
        notificationManager: AndroidNotificationManager,
        channelInfo: NotificationChannelInfo,
    ) {
        val channel =
            NotificationChannel(
                channelInfo.channelId,
                context.getString(channelInfo.nameId),
                channelInfo.importance,
            ).apply {
                description = context.getString(channelInfo.descriptionId)
            }

        notificationManager.createNotificationChannel(channel)
    }

    private fun postNotification(notification: Notification) {
        try {
            val id = Instant.now().epochSecond.toInt()
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
            // Notification permission not granted
            FlamingoLogger.e("Notification permission not granted", e)
        }
    }

    private fun getRandomMissingYouText(
        context: Context,
        senderName: String,
    ): String {
        return when ((0..2).random()) {
            0 -> context.getString(R.string.notification_missingyou_title1, senderName)
            2 -> context.getString(R.string.notification_missingyou_title3, senderName)
            else -> context.getString(R.string.notification_missingyou_title2)
        }
    }
}
