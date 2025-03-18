package pl.kwasow.managers

import android.location.Location
import androidx.lifecycle.LiveData
import pl.kwasow.data.UserLocation

interface LocationManager {
    // ====== Fields
    val userLocation: LiveData<Location?>

    val partnerLocation: LiveData<UserLocation?>

    // ====== Public methods
    suspend fun requestLocation()

    suspend fun requestPartnerLocation(cached: Boolean = true)
}
