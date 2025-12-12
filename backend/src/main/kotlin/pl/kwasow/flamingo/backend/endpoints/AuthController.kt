package pl.kwasow.flamingo.backend.endpoints

import jakarta.transaction.Transactional
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.types.auth.AuthResponse
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.user.UserDto

@Transactional
@RestController
class AuthController {
    // ====== Endpoints
    @GetMapping("/auth")
    fun authenticateUser(
        @AuthenticationPrincipal user: UserDto,
    ): AuthResponse = User(user)
}
