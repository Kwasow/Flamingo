package pl.kwasow.managers

interface MessagingManager {
    // ====== Methods
    suspend fun sendFcmToken(checkAge: Boolean = true)

    suspend fun subscribeToTopics()

    suspend fun sendMissingYou(): Boolean
}
