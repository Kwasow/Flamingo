package pl.kwasow.managers

import androidx.lifecycle.LiveData
import pl.kwasow.flamingo.types.location.UserLocation

interface LocationManager {
    // ====== Fields
    val userLocation: LiveData<UserLocation?>

    val partnerLocation: LiveData<UserLocation?>

    // ====== Public methods
    fun updatePartnerLocation(location: UserLocation)

    suspend fun requestLocation()

    suspend fun requestPartnerLocation()
}
