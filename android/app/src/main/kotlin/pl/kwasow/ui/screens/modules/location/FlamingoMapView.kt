package pl.kwasow.ui.screens.modules.location

import android.location.Location
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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
import pl.kwasow.data.User
import pl.kwasow.data.UserIcon
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
    val user = viewModel.getUser()

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
            CurrentLocationMarker(location = userLoc, user = user)
        }

        val partnerLoc = partnerLocation.value
        if (partnerLoc != null) {
            PersonMarker(location = partnerLoc, user = user)
        }
    }
}

// ====== Private composables
@Composable
private fun CurrentLocationMarker(
    user: User?,
    location: Location,
) {
    FlamingoMarker(
        latitude = location.latitude,
        longitude = location.longitude,
        title = stringResource(id = R.string.module_location_your_location),
        time = location.time,
        icon = user?.icon,
        fallbackIcon = R.drawable.ic_current_location,
    )
}

@Composable
private fun PersonMarker(
    user: User?,
    location: UserLocation,
) {
    FlamingoMarker(
        latitude = location.latitude,
        longitude = location.longitude,
        title = location.userName,
        time = location.timestamp,
        icon = user?.partner?.icon,
        fallbackIcon = R.drawable.ic_map_user_marker,
    )
}

@Composable
private fun FlamingoMarker(
    latitude: Double,
    longitude: Double,
    title: String,
    time: Long,
    icon: UserIcon?,
    @DrawableRes fallbackIcon: Int,
) {
    val markerState = rememberMarkerState(position = LatLng(latitude, longitude))

    MarkerInfoWindowComposable(
        state = markerState,
        title = title,
        infoContent = {
            MarkerInfo(
                name = title,
                time = time,
                icon = icon,
            )
        },
    ) {
        if (icon != null) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = CircleShape,
                modifier =
                    Modifier.border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground,
                        CircleShape,
                    ),
            ) {
                Icon(
                    painter = painterResource(id = icon.res),
                    contentDescription = title,
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(4.dp)
                            .size(16.dp),
                )
            }
        } else {
            Image(
                painter = painterResource(id = fallbackIcon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun MarkerInfo(
    name: String,
    time: Long,
    icon: UserIcon?,
) {
    Card(modifier = Modifier.padding(4.dp)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = FlamingoDateUtils.getRelativeTimeString(time),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                )
            }

            icon?.res?.let { resId ->
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = name,
                    modifier =
                        Modifier
                            .padding(start = 16.dp)
                            .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                            .padding(8.dp)
                            .size(32.dp),
                )
            }
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
        icon = UserIcon.SHEEP,
    )
}
