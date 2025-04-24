package pl.kwasow.managers

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.ui.graphics.Color

interface IntentManager {
    // ====== Methods
    fun setupShortcuts()

    fun getMemoryUrl(): String

    fun getMissingYouUrl(): String

    fun buildPendingIntent(intent: Intent): PendingIntent

    fun buildMissingYouNotificationIntent(): Intent

    fun buildMissingYouShortcutIntent(): Intent

    fun buildMemoryNotificationIntent(): Intent
}
