package pl.kwasow.managers

import android.app.PendingIntent
import android.content.Intent

interface IntentManager {
    // ====== Methods
    fun getMemoryUrl(): String

    fun getMissingYouUrl(): String

    fun buildPendingIntent(intent: Intent): PendingIntent

    fun buildMissingYouNotificationIntent(): Intent

    fun buildMissingYouShortcutIntent(): Intent

    fun buildMemoryNotificationIntent(): Intent
}
