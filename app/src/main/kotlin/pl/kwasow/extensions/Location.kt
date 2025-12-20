package pl.kwasow.extensions

import android.location.Location
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.user.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Location.toUserLocation(user: User): UserLocation =
    UserLocation(
        userId = user.id,
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = this.accuracy,
        lastSeen =
            LocalDateTime.from(
                Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()),
            ),
    )
