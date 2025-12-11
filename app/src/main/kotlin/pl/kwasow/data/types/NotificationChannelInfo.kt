package pl.kwasow.data.types

import android.app.NotificationManager

data class NotificationChannelInfo(
    val nameId: Int,
    val descriptionId: Int,
    val channelId: String,
    val soundId: Int = -1,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
)
