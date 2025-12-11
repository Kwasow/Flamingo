package pl.kwasow.flamingo.types.location

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.serializers.LocalDateTimeSerializer
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Serializable
data class UserLocation(
    val userId: Int,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastSeen: LocalDateTime,
) {
    constructor(dto: UserLocationDto) : this(
        userId = dto.userId,
        latitude = dto.latitude,
        longitude = dto.longitude,
        accuracy = dto.accuracy,
        lastSeen = LocalDateTime.ofInstant(dto.lastSeen.toInstant(), ZoneOffset.UTC),
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
    @Column(name = "last_seen")
    val lastSeen: Timestamp,
) {
    // ====== Constructors
    constructor() : this(0, 0.0, 0.0, 0f, Timestamp.from(Instant.now()))

    constructor(userLocation: UserLocation) : this(
        userId = userLocation.userId,
        latitude = userLocation.latitude,
        longitude = userLocation.longitude,
        accuracy = userLocation.accuracy,
        lastSeen = Timestamp.from(userLocation.lastSeen.toInstant(ZoneOffset.UTC)),
    )
}
