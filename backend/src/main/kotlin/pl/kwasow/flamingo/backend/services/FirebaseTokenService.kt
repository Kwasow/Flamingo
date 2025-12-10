package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.data.FirebaseTokenDto
import pl.kwasow.flamingo.backend.repositories.FirebaseTokenRepository
import java.sql.Timestamp
import java.time.Instant

@Service
class FirebaseTokenService(
    private val firebaseTokenRepository: FirebaseTokenRepository,
) {
    // ====== Public methods
    fun updateTokenForUser(
        token: String,
        debug: Boolean,
        userId: Int,
    ) {
        val now = Timestamp.from(Instant.now())
        val existingEntry = firebaseTokenRepository.findByToken(token)

        if (existingEntry != null) {
            firebaseTokenRepository.save(existingEntry.copy(lastSeen = now))
        } else {
            val newEntry =
                FirebaseTokenDto(
                    id = 0,
                    userId = userId,
                    lastSeen = now,
                    token = token,
                    debug = debug,
                )

            firebaseTokenRepository.save(newEntry)
        }
    }

    fun getAllTokens(): List<FirebaseTokenDto> = firebaseTokenRepository.findAll()

    fun getTokensForUser(userId: Int) = firebaseTokenRepository.findByUserId(userId)

    fun deleteOldTokens() {
        val halfYearAgo = Timestamp.from(Instant.now().minusSeconds(60 * 60 * 24 * 180))

        firebaseTokenRepository.deleteBeforeTimestamp(halfYearAgo)
    }

    fun deleteDevTokens() = firebaseTokenRepository.deleteByDebug(true)
}
