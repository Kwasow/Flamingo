package pl.kwasow.flamingo.backend.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.types.messaging.MessageType
import pl.kwasow.flamingo.types.user.User

@Service
class FirebaseMessagingService(
    private val firebaseMessaging: FirebaseMessaging,
    private val firebaseTokenService: FirebaseTokenService,
) {
    // ====== Public methods
    fun sendMissingYouMessage(user: User): HttpStatus {
        val data =
            mapOf(
                "type" to MessageType.MISSING_YOU.id,
                "name" to user.firstName,
            )

        val partner = user.partner ?: return HttpStatus.BAD_REQUEST

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
    ): HttpStatus {
        val message =
            MulticastMessage
                .builder()
                .putAllData(data)
                .addAllTokens(recipients)
                .build()

        // TODO: Would this benefit for running Async and returning success immediately?
        try {
            firebaseMessaging.sendEachForMulticast(message)
        } catch (_: Exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR
        }

        return HttpStatus.OK
    }
}
