package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.backend.data.FirebaseToken

interface FirebaseTokenRepository : JpaRepository<FirebaseToken, Int> {
    fun findByUserId(userId: Int): List<FirebaseToken>

    fun findByToken(token: String): FirebaseToken?
}
