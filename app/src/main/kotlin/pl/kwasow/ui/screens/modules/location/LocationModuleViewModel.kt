package pl.kwasow.ui.screens.modules.location

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.kwasow.managers.LocationManager
import pl.kwasow.managers.PermissionManager
import pl.kwasow.managers.UserManager

class LocationModuleViewModel(
    private val locationManager: LocationManager,
    private val permissionManager: PermissionManager,
    private val userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    var isLoading by mutableStateOf(false)
    val user = userManager.userFlow
    val userLocation = locationManager.userLocation
    val partnerLocation = locationManager.partnerLocation

    // ====== Constructors
    init {
        viewModelScope.launch {
            if (user.first() == null) {
                userManager.refreshUser()
            }
        }
    }

    // ====== Public methods
    fun refreshUserLocation() {
        viewModelScope.launch {
            isLoading = true

            locationManager.requestLocation()
            locationManager.requestPartnerLocation()

            isLoading = false
        }
    }

    fun launchPermissionSettings(activity: Activity) =
        permissionManager.launchPermissionSettings(activity)
}
