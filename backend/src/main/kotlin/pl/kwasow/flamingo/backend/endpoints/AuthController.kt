package pl.kwasow.flamingo.backend.endpoints

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.types.auth.AuthResponse
import pl.kwasow.flamingo.types.user.User

@RestController
class AuthController {
    // ====== Endpoints
    @GetMapping("/auth")
    fun authenticateUser(
        @AuthenticationPrincipal user: User,
    ): AuthResponse = user
}
