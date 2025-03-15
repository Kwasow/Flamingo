package pl.kwasow.ui.screens.modules.location

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch
import pl.kwasow.managers.LocationManager
import pl.kwasow.managers.PermissionManager

class LocationModuleViewModel(
    private val locationManager: LocationManager,
    private val permissionManager: PermissionManager,
) : ViewModel() {
    // ====== Fields
    var isLoading by mutableStateOf(false)
    val userLocation = locationManager.userLocation
    val partnerLocation = locationManager.partnerLocation

    // ====== Public methods
    fun refreshUserLocation() {
        viewModelScope.launch {
            isLoading = true

            locationManager.requestLocation()
            locationManager.requestPartnerLocation(false)

            isLoading = false
        }
    }

    @ExperimentalPermissionsApi
    @Composable
    fun rememberLocationPermissionState(): MultiplePermissionsState =
        permissionManager.rememberLocationPermissionState()

    fun launchPermissionSettings(activity: Activity) =
        permissionManager.launchPermissionSettings(activity)
}
