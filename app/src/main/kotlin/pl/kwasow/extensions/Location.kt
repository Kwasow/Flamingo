package pl.kwasow.extensions

import android.location.Location
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.user.User
import java.time.Instant
import java.time.LocalDateTime

fun Location.toUserLocation(user: User): UserLocation =
    UserLocation(
        userId = user.id,
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = this.accuracy,
        lastSeen = LocalDateTime.from(Instant.ofEpochSecond(this.time)),
    )
