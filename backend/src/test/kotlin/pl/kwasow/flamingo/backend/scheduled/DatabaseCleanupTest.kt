package pl.kwasow.flamingo.backend.scheduled

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.kwasow.flamingo.backend.data.FirebaseTokenDto
import pl.kwasow.flamingo.backend.repositories.FirebaseTokenRepository
import pl.kwasow.flamingo.backend.setup.BaseTest
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@SpringBootTest
class DatabaseCleanupTest : BaseTest() {
    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @Autowired
    private lateinit var tokenRepository: FirebaseTokenRepository

    @BeforeEach
    fun setupTokens() {
        // FirebaseTokens table contents:
        //  - 3 non-debug, recent tokens (from testdb.sql)
        //  - 1 non-debug, old token
        //  - 1 debug, recent token
        //  - 1 debug, old token
        tokenRepository.save(buildToken(debug = false, old = true))
        tokenRepository.save(buildToken(debug = true, old = false))
        tokenRepository.save(buildToken(debug = true, old = true))
    }

    @Test
    fun `old tokens are removed from the database`() {
        databaseCleanup.cleanupDatabase()

        val tokens = tokenRepository.findAll()

        val cutoff = Timestamp.from(Instant.now().minusSeconds(60 * 60 * 24 * 180))
        assertEquals(4, tokens.size)
        assert(tokens.all { it.lastSeen.after(cutoff) })
    }

    // ====== Private methods
    @OptIn(ExperimentalUuidApi::class)
    private fun buildToken(
        debug: Boolean,
        old: Boolean,
    ): FirebaseTokenDto {
        val nowInstant = Instant.now()
        val oldInstant = nowInstant.minusSeconds(60 * 60 * 24 * 181)

        return FirebaseTokenDto(
            id = 0,
            userId = TestData.BOB_ID,
            lastSeen = Timestamp.from(if (old) oldInstant else nowInstant),
            token = "just-any-token-${Uuid.random()}",
            debug = debug,
        )
    }
}
