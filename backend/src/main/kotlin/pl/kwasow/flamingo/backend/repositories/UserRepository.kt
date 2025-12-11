package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.user.UserDto

interface UserRepository : JpaRepository<UserDto, Int> {
    // ====== Public methods
    fun findByEmail(email: String): UserDto?
}
