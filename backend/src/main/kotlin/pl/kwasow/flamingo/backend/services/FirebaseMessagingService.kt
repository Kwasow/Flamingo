package pl.kwasow.flamingo.backend.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.types.messaging.MessageType
import pl.kwasow.flamingo.types.user.UserDto

@Service
class FirebaseMessagingService(
    private val firebaseMessaging: FirebaseMessaging,
    private val firebaseTokenService: FirebaseTokenService,
) {
    // ====== Public methods
    fun sendMissingYouMessage(user: UserDto): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                "type" to MessageType.MISSING_YOU.id,
                "name" to user.firstName,
            )

        return sendToUser(partner.id, data)
    }

    fun sendLocationUpdatedMessage(user: UserDto): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                "type" to MessageType.LOCATION_UPDATED.id,
            )

        return sendToUser(partner.id, data)
    }

    fun sendLocationRequest(user: UserDto): Boolean {
        val partner = user.partner ?: return false

        val data =
            mapOf(
                "type" to MessageType.REQUEST_LOCATION.id,
            )

        return sendToUser(partner.id, data)
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

        return sendMessage(recipientTokens, data)
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
        firebaseMessaging.sendEachForMulticast(message)

        return true
    }
}
