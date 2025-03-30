package pl.kwasow.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import pl.kwasow.MainActivity
import pl.kwasow.data.enums.InteractionSource

class IntentManagerImpl(
    private val context: Context,
) : IntentManager {
    // ====== Fields
    companion object {
        private const val PREFIX = "flamingo://"

        private const val MEMORY_URL = "$PREFIX/memory/notification"
        private const val MISSING_YOU_URL = "$PREFIX/missingyou"
    }

    // ====== Interface methods
    override fun getMemoryUrl(): String = MEMORY_URL

    override fun getMissingYouUrl(): String = MISSING_YOU_URL

    override fun buildPendingIntent(intent: Intent): PendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

    override fun buildMissingYouNotificationIntent(): Intent =
        buildDataIntent("$MISSING_YOU_URL?source=${InteractionSource.Notification}")

    override fun buildMissingYouShortcutIntent(): Intent =
        buildDataIntent("$MISSING_YOU_URL?source=${InteractionSource.Shortcut}")

    override fun buildMemoryNotificationIntent(): Intent =
        buildDataIntent("$MEMORY_URL?source=${InteractionSource.Notification}")

    // ====== Private methods
    private fun buildDataIntent(stringData: String?): Intent =
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if (stringData != null) {
                data = stringData.toUri()
            }
        }
}
