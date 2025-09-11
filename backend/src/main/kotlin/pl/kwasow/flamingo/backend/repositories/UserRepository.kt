package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.user.User

interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}
