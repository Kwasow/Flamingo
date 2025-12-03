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
        val data =
            mapOf(
                "type" to MessageType.MISSING_YOU.id,
                "name" to user.firstName,
            )

        val partner = user.partner ?: return false

        val recipientTokens =
            firebaseTokenService
                .getTokensForUser(partner.id)
                .map { it.token }

        return sendMessage(recipientTokens, data)
    }

    // ====== Private methods
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
