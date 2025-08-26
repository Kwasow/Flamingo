package pl.kwasow.flamingo.backend.endpoints

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.UserService
import pl.kwasow.flamingo.types.user.User

@RestController
class AuthController(
    private val userService: UserService,
) {
    @GetMapping("/auth")
    fun authenticateUser(@AuthenticationPrincipal email: String): ResponseEntity<User> {
        val userDetails = userService.getUserByEmail(email)

        return if (userDetails == null) {
            ResponseEntity.internalServerError().build()
        } else {
            ResponseEntity.ok(userDetails)
        }
    }
}
