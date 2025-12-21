package pl.kwasow.flamingo.types.messaging

enum class MessagingKeys(
    val key: String,
) {
    TYPE("type"),
    LOCATION_JSON("user_location_json"),
    MISSING_YOU_NAME("name"),
}
