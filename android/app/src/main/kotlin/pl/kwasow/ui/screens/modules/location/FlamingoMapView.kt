package pl.kwasow.ui.screens.modules.location

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.UserLocation

// ====== Public composables
@Composable
fun FlamingoMapView(
    hazeState: HazeState,
    paddingValues: PaddingValues,
) {
    val viewModel = koinViewModel<LocationModuleViewModel>()
    val userLocation = viewModel.userLocation.observeAsState()
    val partnerLocation = viewModel.partnerLocation.observeAsState()

    val warsaw = LatLng(52.229845, 21.0104188)
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(warsaw, 10f)
        }

    LaunchedEffect(userLocation.value) {
        val location = userLocation.value ?: return@LaunchedEffect
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                15f,
            ),
        )
    }

    LaunchedEffect(true) {
        viewModel.refreshUserLocation()
    }

    GoogleMap(
        modifier =
            Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
        cameraPositionState = cameraPositionState,
        uiSettings =
            MapUiSettings(
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
            ),
        contentPadding = paddingValues,
    ) {
        val userLoc = userLocation.value
        if (userLoc != null) {
            CurrentLocationMarker(location = userLoc)
        }

        val partnerLoc = partnerLocation.value
        if (partnerLoc != null) {
            PersonMarker(location = partnerLoc)
        }
    }
}

// ====== Private composables
@Composable
private fun CurrentLocationMarker(location: Location) {
    val markerState = rememberMarkerState(position = LatLng(location.latitude, location.longitude))

    MarkerComposable(
        state = markerState,
        title = stringResource(id = R.string.module_location_your_location),
        onClick = { false },
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_current_location),
            contentDescription = stringResource(id = R.string.module_location_your_location),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun PersonMarker(location: UserLocation) {
    val markerState = rememberMarkerState(position = LatLng(location.latitude, location.longitude))

    MarkerComposable(
        state = markerState,
        title = location.userName,
        onClick = { false },
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_map_user_marker),
            contentDescription = location.userName,
            modifier = Modifier.size(16.dp),
        )
    }
}
