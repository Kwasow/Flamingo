package pl.kwasow.flamingo.backend.endpoints.location

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.kwasow.flamingo.backend.services.FirebaseMessagingService
import pl.kwasow.flamingo.backend.services.UserLocationService
import pl.kwasow.flamingo.types.location.LocationGetResponse
import pl.kwasow.flamingo.types.location.LocationUpdateResponse
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.user.User

@RestController
class LocationController(
    private val firebaseMessagingService: FirebaseMessagingService,
    private val locationService: UserLocationService,
) {
    // ====== Endpoints
    @GetMapping("/location/get/partner")
    fun getPartnerLocation(
        @AuthenticationPrincipal user: User,
    ): LocationGetResponse {
        val partnerId = user.partner?.id

        if (partnerId != null) {
            firebaseMessagingService.sendLocationRequest(user)

            return locationService.getUserLocation(partnerId)?.let { UserLocation(it) }
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/location/get/self")
    fun getSelfLocation(
        @AuthenticationPrincipal user: User,
    ): LocationGetResponse = locationService.getUserLocation(user.id)?.let { UserLocation(it) }

    @PostMapping("/location/update")
    fun updateLocation(
        @AuthenticationPrincipal user: User,
        @RequestBody location: UserLocation,
    ): LocationUpdateResponse {
        if (locationService.verifyLocationForUpdating(user, location)) {
            val locationDto = locationService.updateUserLocation(location)
            firebaseMessagingService.sendLocationUpdatedMessage(user, location)

            return UserLocation(locationDto)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}
