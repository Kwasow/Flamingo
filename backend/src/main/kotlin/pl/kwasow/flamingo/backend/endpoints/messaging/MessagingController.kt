package pl.kwasow.flamingo.backend.endpoints.messaging

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.FirebaseTokenService
import pl.kwasow.flamingo.types.messaging.FcmSendMessageRequest
import pl.kwasow.flamingo.types.messaging.FcmUpdateTokenRequest
import pl.kwasow.flamingo.types.user.User

@RestController
class MessagingController(
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
    }
}
