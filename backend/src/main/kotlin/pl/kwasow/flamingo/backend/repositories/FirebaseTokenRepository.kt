package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.backend.data.FirebaseTokenDto

interface FirebaseTokenRepository : JpaRepository<FirebaseTokenDto, Int> {
    fun findByUserId(userId: Int): List<FirebaseTokenDto>

    fun findByToken(token: String): FirebaseTokenDto?

    fun deleteByDebug(debug: Boolean)
}
