package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.UserRepository
import pl.kwasow.flamingo.types.user.UserDto

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    // ====== Public methods
    fun getUserByEmail(email: String): UserDto? = userRepository.findByEmail(email)
}
