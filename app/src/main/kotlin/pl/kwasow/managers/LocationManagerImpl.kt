package pl.kwasow.managers

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.utils.FlamingoLogger

class LocationManagerImpl(
    context: Context,
    private val requestManager: RequestManager,
    private val preferencesManager: PreferencesManager,
) : LocationManager {
    // ====== Fields
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override val userLocation = MutableLiveData<Location?>(null)
    override val partnerLocation = MutableLiveData<UserLocation?>(null)

    // ====== Interface methods
    override suspend fun requestLocation() {
        if (userLocation.value == null) {
            userLocation.postValue(getCachedLocation())
        }

        val location = getCurrentLocation()
        if (location != null) {
            userLocation.postValue(location)
        }

        updateLocationOnServer(location)
    }

    override suspend fun requestPartnerLocation() {
        // If the user didn't allow background location requests, we'll only allow them to
        // request the server cached location
        val partnerLocation = requestManager.getPartnerLocation()
        if (partnerLocation != null) {
            this.partnerLocation.postValue(partnerLocation)
        }
    }

    // ====== Private methods
    private suspend fun getCachedLocation(): Location? {
        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            updateLocationOnServer(location)

            return location
        } catch (e: SecurityException) {
            FlamingoLogger.e("Location permission not granted", e)
            return null
        } catch (e: Exception) {
            FlamingoLogger.e("Error getting location", e)
            return null
        }
    }

    private suspend fun getCurrentLocation(): Location? {
        val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY

        try {
            val location: Location? =
                fusedLocationClient.getCurrentLocation(
                    accuracy,
                    CancellationTokenSource().token,
                ).await()
            updateLocationOnServer(location)

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

        if (requestManager.updateLocation(location)) {
            FlamingoLogger.i("Location updated on server")
        } else {
            FlamingoLogger.e("Location update on server failed")
        }
    }
}
