package pl.kwasow.sunshine.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.kwasow.sunshine.managers.LocationManager
import pl.kwasow.sunshine.managers.MemoriesManager
import pl.kwasow.sunshine.managers.MessagingManager
import pl.kwasow.sunshine.managers.NotificationManager
import pl.kwasow.sunshine.utils.SunshineLogger

class SunshineMessagingService : FirebaseMessagingService() {
    // ====== Fields
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val locationManager by lazy { inject<LocationManager>() }
    private val memoriesManager by lazy { inject<MemoriesManager>() }
    private val notificationManager by lazy { inject<NotificationManager>() }

    // ====== Interface methods
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        SunshineLogger.d("FCM token updated")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        when (MessagingManager.MessageType.fromString(data["type"] ?: "")) {
            MessagingManager.MessageType.MISSING_YOU -> handleMissingYouMessage(data)
            MessagingManager.MessageType.DAILY_MEMORY -> handleDailyMemoryMessage()
            MessagingManager.MessageType.REQUEST_LOCATION -> handleRequestLocationMessage()
            else -> handleIncorrectMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    // ====== Private methods
    private fun handleMissingYouMessage(data: Map<String, String>) {
        val senderName = data["name"] ?: return handleIncorrectMessage()
        notificationManager.value.postMissingYouNotification(senderName)
    }

    private fun handleDailyMemoryMessage() {
        scope.launch {
            if (memoriesManager.value.getTodayMemories().isNotEmpty()) {
                notificationManager.value.postMemoryNotification()
            }
        }
    }

    private fun handleRequestLocationMessage() {
        scope.launch {
            locationManager.value.sendLocationToPartner()
        }
    }

    private fun handleIncorrectMessage() {
        SunshineLogger.e("Received incorrect Firebase message")
    }
}
