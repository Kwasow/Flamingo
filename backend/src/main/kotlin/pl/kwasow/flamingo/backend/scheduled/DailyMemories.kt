package pl.kwasow.flamingo.backend.scheduled

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pl.kwasow.flamingo.backend.services.FirebaseMessagingService
import java.util.concurrent.TimeUnit

@Component
class DailyMemories(
    private val firebaseMessaging: FirebaseMessagingService,
) {
    // ====== Fields
    companion object {
        private val logger = LoggerFactory.getLogger(DailyMemories::class.java)
    }

    // ====== Public methods
//    @Scheduled(cron = "0 30 7 * * *")
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    fun sendNotifications() {
        logger.info("Sending daily notifications...")

        firebaseMessaging.sendMemoryNotification()

        logger.info("Done")
    }
}
