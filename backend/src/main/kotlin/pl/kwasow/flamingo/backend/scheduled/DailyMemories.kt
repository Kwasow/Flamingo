package pl.kwasow.flamingo.backend.scheduled

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DailyMemories {
    // ====== Fields
    companion object {
        private val logger = LoggerFactory.getLogger(DailyMemories::class.java)
    }

    // ====== Public methods
    @Scheduled(cron = "0 30 7 * * *")
    fun sendNotifications() {
        logger.info("Sending daily notifications...")

        logger.info("Done")
    }
}