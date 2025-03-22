package pl.kwasow.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.kwasow.managers.LocationManager
import pl.kwasow.managers.MemoriesManager
import pl.kwasow.managers.MessagingManager
import pl.kwasow.managers.NotificationManager
import pl.kwasow.managers.SettingsManager
import pl.kwasow.utils.FlamingoLogger

class FlamingoMessagingService : FirebaseMessagingService() {
    // ====== Fields
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val locationManager by inject<LocationManager>()
    private val memoriesManager by inject<MemoriesManager>()
    private val notificationManager by inject<NotificationManager>()
    private val settingsManager by inject<SettingsManager>()

    // ====== Interface methods
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        FlamingoLogger.d("FCM token updated")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        when (MessagingManager.MessageType.fromString(data["type"] ?: "")) {
            MessagingManager.MessageType.MISSING_YOU -> handleMissingYouMessage(data)
            MessagingManager.MessageType.DAILY_MEMORY -> handleDailyMemoryMessage()
            MessagingManager.MessageType.REQUEST_LOCATION -> handleRequestLocationMessage()
            MessagingManager.MessageType.LOCATION_UPDATED -> handleLocationUpdatedMessage()
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
        notificationManager.postMissingYouNotification(senderName)
    }

    private fun handleDailyMemoryMessage() {
        scope.launch {
            if (memoriesManager.getTodayMemories().isNotEmpty()) {
                notificationManager.postMemoryNotification()
            }
        }
    }

    private fun handleRequestLocationMessage() {
        if (!settingsManager.allowLocationRequests) {
            return
        }

        scope.launch {
            locationManager.requestLocation()
        }
    }

    private fun handleLocationUpdatedMessage() {
        // We want the cached location in this case, to prevent an infinite loop of location
        // requests
        scope.launch {
            locationManager.requestPartnerLocation(true)
        }
    }

    private fun handleIncorrectMessage() {
        FlamingoLogger.e("Received incorrect Firebase message")
    }
}
