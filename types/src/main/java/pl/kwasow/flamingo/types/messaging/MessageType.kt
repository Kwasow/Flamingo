package pl.kwasow.flamingo.types.messaging

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType(
    val id: String,
) {
    MISSING_YOU("missing_you"),
    DAILY_MEMORY("daily_memory"),
    REQUEST_LOCATION("request_location"),
    LOCATION_UPDATED("location_updated"),
    ;

    companion object {
        fun fromString(value: String): MessageType? = MessageType.entries.find { it.id == value }
    }
}
