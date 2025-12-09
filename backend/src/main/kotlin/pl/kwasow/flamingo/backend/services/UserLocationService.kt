package pl.kwasow.flamingo.backend.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.UserLocationRepository
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.location.UserLocationDto
import pl.kwasow.flamingo.types.user.UserDto

@Service
class UserLocationService(
    private val userLocationRepository: UserLocationRepository,
) {
    fun getUserLocation(userId: Int): UserLocationDto? =
        userLocationRepository.findByIdOrNull(userId)

    fun updateUserLocation(location: UserLocationDto) = userLocationRepository.save(location)

    fun verifyLocationForUpdating(
        user: UserDto,
        location: UserLocation,
    ): Boolean = location.userId == user.id
}
