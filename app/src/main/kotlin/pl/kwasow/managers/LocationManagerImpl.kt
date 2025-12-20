package pl.kwasow.managers

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import pl.kwasow.extensions.toUserLocation
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.utils.FlamingoLogger

class LocationManagerImpl(
    context: Context,
    private val requestManager: RequestManager,
    private val userManager: UserManager,
) : LocationManager {
    // ====== Fields
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override val userLocation = MutableLiveData<UserLocation?>(null)
    override val partnerLocation = MutableLiveData<UserLocation?>(null)

    // ====== Interface methods
    override fun updatePartnerLocation(location: UserLocation) {
        partnerLocation.postValue(location)
    }

    override suspend fun requestLocation() {
        val user = userManager.user.value ?: return

        val location = getCurrentLocation()
        if (location != null) {
            userLocation.postValue(location.toUserLocation(user))
        }

        updateLocationOnServer(location)
    }

    override suspend fun requestPartnerLocation() {
        val partnerLocation = requestManager.getPartnerLocation()
        if (partnerLocation != null) {
            this.partnerLocation.postValue(partnerLocation)
        }
    }

    // ====== Private methods
    private suspend fun getCurrentLocation(): Location? {
        val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY

        try {
            val location: Location? =
                fusedLocationClient.getCurrentLocation(
                    accuracy,
                    CancellationTokenSource().token,
                ).await()

            return location
        } catch (e: SecurityException) {
            FlamingoLogger.e("Location permission not granted", e)
            return null
        } catch (e: Exception) {
            FlamingoLogger.e("Error getting location", e)
            return null
        }
    }

    private suspend fun updateLocationOnServer(location: Location?) {
        if (location == null) {
            return
        }

        val user = userManager.user.value ?: return

        if (requestManager.updateLocation(location.toUserLocation(user))) {
            FlamingoLogger.i("Location updated on server")
        } else {
            FlamingoLogger.e("Location update on server failed")
        }
    }
}
