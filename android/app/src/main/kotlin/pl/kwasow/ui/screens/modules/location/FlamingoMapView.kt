package pl.kwasow.ui.screens.modules.location

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.UserLocation
import pl.kwasow.utils.FlamingoDateUtils

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
private fun CurrentLocationMarker(
    location: Location,
    onClick: () -> Boolean = { false },
) {
    val markerState = rememberMarkerState(position = LatLng(location.latitude, location.longitude))
    val title = stringResource(id = R.string.module_location_your_location)

    MarkerInfoWindowComposable(
        state = markerState,
        title = title,
        onClick = { onClick() },
        infoContent = { MarkerInfo(name = title, time = location.time) },
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_current_location),
            contentDescription = stringResource(id = R.string.module_location_your_location),
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun PersonMarker(
    location: UserLocation,
    onClick: () -> Boolean = { false },
) {
    val markerState = rememberMarkerState(position = LatLng(location.latitude, location.longitude))

    MarkerInfoWindowComposable(
        state = markerState,
        title = location.userName,
        onClick = { onClick() },
        infoContent = { MarkerInfo(name = location.userName, time = location.timestamp) },
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_map_user_marker),
            contentDescription = location.userName,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun MarkerInfo(
    name: String,
    time: Long,
) {
    Card {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(FlamingoDateUtils.getRelativeTimeString(time))
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun MarkerInfoPreview() {
    MarkerInfo(
        name = "My location",
        time = System.currentTimeMillis() - 1000000,
    )
}
