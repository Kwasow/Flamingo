package pl.kwasow.flamingo.types.location

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Serializable
data class UserLocation(
    val userId: Int,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
) {
    constructor(dto: UserLocationDto) : this(
        userId = dto.userId,
        latitude = dto.latitude,
        longitude = dto.longitude,
        accuracy = dto.accuracy,
        timestamp = dto.timestamp,
    )
}

@Entity
@Table(name = "UserLocations")
data class UserLocationDto(
    @Id
    @Column(name = "user_id")
    val userId: Int,
    @Column(name = "latitude")
    val latitude: Double,
    @Column(name = "longitude")
    val longitude: Double,
    @Column(name = "accuracy")
    val accuracy: Float,
    @Column(name = "time_stamp")
    val timestamp: Long,
) {
    // ====== Constructors
    constructor() : this(0, 0.0, 0.0, 0f, 0)

    constructor(userLocation: UserLocation) : this(
        userId = userLocation.userId,
        latitude = userLocation.latitude,
        longitude = userLocation.longitude,
        accuracy = userLocation.accuracy,
        timestamp = userLocation.timestamp,
    )
}
