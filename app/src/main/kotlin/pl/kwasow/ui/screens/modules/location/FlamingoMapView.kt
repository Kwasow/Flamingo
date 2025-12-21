package pl.kwasow.ui.screens.modules.location

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.google.maps.android.compose.rememberUpdatedMarkerState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.types.UserIconDetails
import pl.kwasow.extensions.details
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.user.MinimalUser
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.user.UserIcon
import pl.kwasow.utils.FlamingoDateUtils
import java.time.LocalDateTime

// ====== Public composables
@Composable
fun FlamingoMapView(
    hazeState: HazeState,
    paddingValues: PaddingValues,
) {
    val viewModel = koinViewModel<LocationModuleViewModel>()
    val userLocation = viewModel.userLocation.observeAsState()
    val partnerLocation = viewModel.partnerLocation.observeAsState()
    val user = viewModel.user.collectAsState(null)

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
            CurrentLocationMarker(location = userLoc, user = user.value)
        }

        val partnerLoc = partnerLocation.value
        if (partnerLoc != null) {
            PersonMarker(location = partnerLoc, user = user.value)
        }
    }
}

// ====== Private composables
@Composable
private fun CurrentLocationMarker(
    location: UserLocation,
    user: User?,
) {
    FlamingoMarker(
        latitude = location.latitude,
        longitude = location.longitude,
        title = stringResource(id = R.string.module_location_your_location),
        lastSeen = location.lastSeen,
        icon = user?.icon?.details(),
        fallbackIcon = Icons.Default.MyLocation,
    )
}

@Composable
private fun PersonMarker(
    location: UserLocation,
    user: MinimalUser?,
) {
    FlamingoMarker(
        latitude = location.latitude,
        longitude = location.longitude,
        title = user?.firstName ?: "[TODO]",
        lastSeen = location.lastSeen,
        // TODO: Fix this
        icon = user?.icon?.details() ?: UserIcon.SHEEP.details(),
        fallbackIcon = Icons.Default.LocationOn,
    )
}

@Composable
private fun FlamingoMarker(
    latitude: Double,
    longitude: Double,
    title: String,
    lastSeen: LocalDateTime,
    icon: UserIconDetails?,
    fallbackIcon: ImageVector,
) {
    val markerState = rememberUpdatedMarkerState(position = LatLng(latitude, longitude))

    MarkerInfoWindowComposable(
        state = markerState,
        title = title,
        infoContent = {
            MarkerInfo(
                name = title,
                lastSeen = lastSeen,
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
                imageVector = fallbackIcon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun MarkerInfo(
    name: String,
    lastSeen: LocalDateTime,
    icon: UserIconDetails?,
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
                    text = FlamingoDateUtils.getRelativeTimeString(lastSeen),
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
        lastSeen = LocalDateTime.now().minusMinutes(16),
        icon = UserIconDetails(R.drawable.ic_sheep, R.string.contentDescription_sheep_icon),
    )
}
