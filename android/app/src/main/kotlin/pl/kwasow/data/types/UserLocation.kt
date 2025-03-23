package pl.kwasow.data.types

import android.location.Location
import kotlinx.serialization.Serializable

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
    constructor(user: MinimalUser, location: Location, isCached: Boolean) : this(
        user.id,
        user.firstName,
        location.latitude,
        location.longitude,
        location.accuracy,
        location.time,
        isCached,
    )
}
