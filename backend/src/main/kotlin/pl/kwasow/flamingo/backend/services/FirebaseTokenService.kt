package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.data.FirebaseTokenDto
import pl.kwasow.flamingo.backend.repositories.FirebaseTokenRepository
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
        val timestamp = Instant.now().epochSecond
        val existingEntry = firebaseTokenRepository.findByToken(token)

        if (existingEntry != null) {
            firebaseTokenRepository.save(existingEntry.copy(timestamp = timestamp))
        } else {
            val newEntry =
                FirebaseTokenDto(
                    0,
                    userId,
                    timestamp,
                    token,
                    debug,
                )

            firebaseTokenRepository.save(newEntry)
        }
    }

    fun getTokensForUser(userId: Int) = firebaseTokenRepository.findByUserId(userId)


    fun deleteOldTokens() {

    }

    fun deleteDevTokens() = firebaseTokenRepository.deleteByDebug(true)
}
