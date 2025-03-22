package pl.kwasow.managers

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await

class MessagingManagerImpl(
    private val requestManager: RequestManager,
    private val settingsManager: SettingsManager,
    private val userManager: UserManager,
) : MessagingManager {
    // ====== Fields
    companion object {
        private const val MONTH: Long = 1000L * 60L * 60L * 24L * 30L
    }

    private val firebaseMessaging = Firebase.messaging

    // ====== Interface methods
    override suspend fun sendFcmToken(checkAge: Boolean) {
        val lastSyncTime = settingsManager.lastFCMTokenSyncTimestamp
        val currentTime = System.currentTimeMillis()

        if (!checkAge || currentTime - lastSyncTime > MONTH) {
            val token = Firebase.messaging.token.await()

            if (requestManager.updateFcmToken(token)) {
                settingsManager.lastFCMTokenSyncTimestamp = currentTime
            }
        }
    }

    override suspend fun subscribeToTopics() {
        subscribeToAllTopic()
    }

    override suspend fun sendMissingYou(): Boolean = requestManager.sendMissingYouMessage()

    // ====== Private methods
    private fun subscribeToAllTopic() {
        firebaseMessaging.subscribeToTopic("all")
    }
}
