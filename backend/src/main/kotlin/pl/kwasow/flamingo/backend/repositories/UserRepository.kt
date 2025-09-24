package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.user.User

interface UserRepository : JpaRepository<User, Int> {
    // ====== Public methods
    fun findByEmail(email: String): User?
}
