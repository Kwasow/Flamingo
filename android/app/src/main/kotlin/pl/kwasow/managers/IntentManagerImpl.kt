package pl.kwasow.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import pl.kwasow.MainActivity

class IntentManagerImpl(
    private val context: Context,
) : IntentManager {
    // ====== Fields
    companion object {
        private const val PREFIX = "flamingo://"

        const val MEMORY_NOTIFICATION_URL = "$PREFIX/memory/notification"
        const val MISSING_YOU_NOTIFICATION_URL = "$PREFIX/missingyou?source=notification"
        const val MISSING_YOU_SHORTCUT_URL = "$PREFIX/missingyou?source=shortcut"
    }

    // ====== Interface methods
    override fun buildPendingIntent(intent: Intent): PendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

    override fun buildMissingYouNotificationIntent(): Intent =
        buildDataIntent(MISSING_YOU_NOTIFICATION_URL)

    override fun buildMissingYouShortcutIntent(): Intent = buildDataIntent(MISSING_YOU_SHORTCUT_URL)

    override fun buildMemoryNotificationIntent(): Intent = buildDataIntent(MEMORY_NOTIFICATION_URL)

    // ====== Private methods
    private fun buildDataIntent(stringData: String?): Intent =
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if (stringData != null) {
                data = stringData.toUri()
            }
        }
}
