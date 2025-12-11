package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import pl.kwasow.flamingo.backend.data.FirebaseTokenDto
import java.sql.Timestamp

interface FirebaseTokenRepository : JpaRepository<FirebaseTokenDto, Int> {
    fun findByUserId(userId: Int): List<FirebaseTokenDto>

    fun findByToken(token: String): FirebaseTokenDto?

    fun deleteByDebug(debug: Boolean)

    @Modifying
    @Query("DELETE FROM FirebaseTokenDto WHERE lastSeen < :timestamp")
    fun deleteBeforeTimestamp(timestamp: Timestamp)
}
