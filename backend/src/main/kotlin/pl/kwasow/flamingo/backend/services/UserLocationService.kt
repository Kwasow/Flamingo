package pl.kwasow.flamingo.backend.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.UserLocationRepository
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.location.UserLocationDto
import pl.kwasow.flamingo.types.user.UserDto
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class UserLocationService(
    private val userLocationRepository: UserLocationRepository,
) {
    fun getUserLocation(userId: Int): UserLocationDto? =
        userLocationRepository.findByIdOrNull(userId)

    fun updateUserLocation(location: UserLocation): UserLocationDto {
        val nowLocation = location.copy(lastSeen = LocalDateTime.now(ZoneOffset.UTC))

        return userLocationRepository.save(UserLocationDto(nowLocation))
    }

    fun verifyLocationForUpdating(
        user: UserDto,
        location: UserLocation,
    ): Boolean {
        val userCorrect = location.userId == user.id
        val latitudeCorrect = location.latitude in -180.0..180.0
        val longitudeCorrect = location.longitude in -180.0..180.0

        return userCorrect && latitudeCorrect && longitudeCorrect
    }
}
