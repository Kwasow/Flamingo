package pl.kwasow.flamingo.backend.endpoints.messaging

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.kwasow.flamingo.backend.services.FirebaseMessagingService
import pl.kwasow.flamingo.backend.services.FirebaseTokenService
import pl.kwasow.flamingo.types.messaging.FcmSendMessageRequest
import pl.kwasow.flamingo.types.messaging.FcmUpdateTokenRequest
import pl.kwasow.flamingo.types.messaging.MessageType
import pl.kwasow.flamingo.types.user.User

@RestController
class MessagingController(
    private val firebaseMessagingService: FirebaseMessagingService,
    private val firebaseTokenService: FirebaseTokenService,
) {
    // ====== Endpoints
    @PostMapping("/messaging/updateFcmToken")
    fun updateToken(
        @AuthenticationPrincipal user: User,
        @RequestBody tokenDetails: FcmUpdateTokenRequest,
    ) = firebaseTokenService.updateTokenForUser(tokenDetails.token, tokenDetails.debug, user.id)

    @PostMapping("/messaging/send")
    fun sendMessage(
        @AuthenticationPrincipal user: User,
        @RequestBody message: FcmSendMessageRequest,
    ) {
        val result =
            when (message) {
                MessageType.MISSING_YOU -> firebaseMessagingService.sendMissingYouMessage(user)
                else -> false
            }

        if (!result) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }
}
