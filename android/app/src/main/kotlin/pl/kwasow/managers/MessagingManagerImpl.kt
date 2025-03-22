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
        val token = Firebase.messaging.token.await()

        val lastSyncTime = settingsManager.lastFCMTokenSyncTimestamp
        val currentTime = System.currentTimeMillis()

        if (!checkAge || currentTime - lastSyncTime > MONTH) {
            requestManager.updateFcmToken(token)
            settingsManager.lastFCMTokenSyncTimestamp = currentTime
        }
    }

    override suspend fun subscribeToTopics() {
        subscribeToUserTopic()
        subscribeToAllTopic()
    }

    override suspend fun sendMissingYou(): Boolean = requestManager.sendMissingYouMessage()

    // ====== Private methods
    private fun subscribeToAllTopic() {
        firebaseMessaging.subscribeToTopic("all")
    }

    private suspend fun subscribeToUserTopic() {
        val user = userManager.getUser()
        user?.userTopic?.let { userTopic ->
            firebaseMessaging.subscribeToTopic(userTopic)
        }
    }
}
