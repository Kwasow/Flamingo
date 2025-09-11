package pl.kwasow.flamingo.types.location

import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.types.user.MinimalUser

@Serializable
data class UserLocation(
    val userId: Int,
    val userName: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val isCached: Boolean,
) {
    constructor(
        user: MinimalUser,
        latitude: Double,
        longitude: Double,
        accuracy: Float,
        timestamp: Long,
        isCached: Boolean,
    ) : this(
        user.id,
        user.firstName,
        latitude,
        longitude,
        accuracy,
        timestamp,
        isCached,
    )
}
