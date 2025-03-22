package pl.kwasow.managers

interface MessagingManager {
    // ====== Classes
    enum class MessageType(val id: String) {
        MISSING_YOU("missing_you"),
        DAILY_MEMORY("daily_memory"),
        REQUEST_LOCATION("request_location"),
        LOCATION_UPDATED("location_updated"),
        ;

        companion object {
            fun fromString(value: String): MessageType? = entries.find { it.id == value }
        }
    }

    // ====== Methods
    suspend fun sendFcmToken(checkAge: Boolean = true)

    suspend fun subscribeToTopics()

    suspend fun sendMissingYou(): Boolean
}
