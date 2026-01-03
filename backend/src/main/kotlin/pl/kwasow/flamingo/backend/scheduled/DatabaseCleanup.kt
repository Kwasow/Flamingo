package pl.kwasow.flamingo.backend.scheduled

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.kwasow.flamingo.backend.services.FirebaseTokenService

@Component
class DatabaseCleanup(
    private val firebaseTokenService: FirebaseTokenService,
) {
    // ====== Fields
    companion object {
        private val logger = LoggerFactory.getLogger(DatabaseCleanup::class.java)
    }

    // ====== Public methods
    @Transactional
//    @Scheduled(cron = "0 0 1 * * *")
    @Scheduled(cron = "0 * * * * *")
    fun cleanupDatabase() {
        logger.info("Database cleanup starting...")

        firebaseTokenService.deleteOldTokens()

        logger.info("Done")
    }
}
