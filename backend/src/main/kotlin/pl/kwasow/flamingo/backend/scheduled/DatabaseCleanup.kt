package pl.kwasow.flamingo.backend.scheduled

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
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
    @Scheduled(cron = "0 0 1 * * *")
    fun cleanupDatabase() {
        logger.info("Database cleanup starting...")

        firebaseTokenService.deleteOldTokens()

        logger.info("Done")
    }

    @Scheduled(cron = "0 0 1 6 * *")
    fun cleanupDevDatabase() {
        logger.info("[DEV] Database cleanup starting...")

        firebaseTokenService.deleteDevTokens()

        logger.info("[DEV] Done")
    }
}
