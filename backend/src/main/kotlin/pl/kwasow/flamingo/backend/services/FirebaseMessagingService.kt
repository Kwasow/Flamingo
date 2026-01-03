package pl.kwasow.flamingo.backend.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.extensions.showFailed
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.messaging.MessageType
import pl.kwasow.flamingo.types.messaging.MessagingKeys
import pl.kwasow.flamingo.types.user.User

@Service
class FirebaseMessagingService(
    private val firebaseMessaging: FirebaseMessaging,
    private val firebaseTokenService: FirebaseTokenService,
) {
    // ====== Fields
    companion object {
        private val logger = LoggerFactory.getLogger(FirebaseMessagingService::class.java)
    }

    // ====== Public methods
    fun sendMissingYouMessage(user: User): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                MessagingKeys.TYPE.key to MessageType.MISSING_YOU.id,
                MessagingKeys.MISSING_YOU_NAME.key to user.firstName,
            )

        return sendToUser(partner.id, data)
    }

    fun sendLocationUpdatedMessage(
        user: User,
        location: UserLocation,
    ): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                MessagingKeys.TYPE.key to MessageType.LOCATION_UPDATED.id,
                MessagingKeys.LOCATION_JSON.key to Json.encodeToString(location),
            )

        return sendToUser(partner.id, data)
    }

    fun sendLocationRequest(user: User): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                MessagingKeys.TYPE.key to MessageType.REQUEST_LOCATION.id,
            )

        return sendToUser(partner.id, data)
    }

    fun sendMemoryNotification(): Boolean {
        val tokens = firebaseTokenService.getAllTokens().map { it.token }

        val data =
            mapOf(
                MessagingKeys.TYPE.key to MessageType.DAILY_MEMORY.id,
            )

        return sendMessage(tokens, data)
    }

    // ====== Private methods
    private fun sendToUser(
        userId: Int,
        data: Map<String, String>,
    ): Boolean {
        val recipientTokens =
            firebaseTokenService
                .getTokensForUser(userId)
                .map { it.token }

        return if (recipientTokens.isEmpty()) {
            false
        } else {
            sendMessage(recipientTokens, data)
        }
    }

    private fun sendMessage(
        recipients: Collection<String>,
        data: Map<String, String>,
    ): Boolean {
        val message =
            MulticastMessage
                .builder()
                .putAllData(data)
                .addAllTokens(recipients)
                .build()

        // TODO: Would this benefit for running Async and returning success immediately?
        val res = firebaseMessaging.sendEachForMulticast(message)

        if (res.failureCount != 0) {
            logger.info("FCM multicast result: ${res.showFailed()}")
        }

        return true
    }
}
