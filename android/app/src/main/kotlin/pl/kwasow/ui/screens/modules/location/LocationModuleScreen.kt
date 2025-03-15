package pl.kwasow.ui.screens.modules.location

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.AnimatedRefreshButton
import pl.kwasow.ui.components.FlamingoTopAppBar
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun LocationModuleScreen() {
    val navigation = LocalFlamingoNavigation.current
    val hazeState = remember { HazeState() }
    val style = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surface)

    Scaffold(
        topBar = {
            AppBar(
                onBackPressed = navigation.navigateBack,
                hazeState = hazeState,
                style = style,
            )
        },
    ) { paddingValues ->
        MainView(
            paddingValues = paddingValues,
            hazeState = hazeState,
        )
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun AppBar(
    onBackPressed: () -> Unit,
    hazeState: HazeState,
    style: HazeStyle,
) {
    val viewModel = koinViewModel<LocationModuleViewModel>()
    val locationPermission = viewModel.rememberLocationPermissionState()

    FlamingoTopAppBar(
        title = stringResource(LocationModuleInfo.nameId),
        onBackPressed = onBackPressed,
        modifier = Modifier.hazeEffect(state = hazeState, style = style),
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
        actions = {
            if (locationPermission.allPermissionsGranted) {
                AnimatedRefreshButton(
                    onRefresh = { viewModel.refreshUserLocation() },
                    isRefreshing = viewModel.isLoading,
                )
            }
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainView(
    paddingValues: PaddingValues,
    hazeState: HazeState,
) {
    val viewModel = koinViewModel<LocationModuleViewModel>()
    val locationPermission = viewModel.rememberLocationPermissionState()

    LaunchedEffect(true) {
        locationPermission.launchMultiplePermissionRequest()
    }

    if (locationPermission.allPermissionsGranted) {
        FlamingoMapView(
            hazeState = hazeState,
            paddingValues = paddingValues,
        )
    } else {
        LocationPermissionNotGranted(paddingValues = paddingValues)
    }
}

@Composable
private fun LocationPermissionNotGranted(paddingValues: PaddingValues) {
    val viewModel = koinViewModel<LocationModuleViewModel>()
    val activity = LocalContext.current as Activity

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            stringResource(R.string.module_location_no_permission),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 36.dp),
        )

        Button(
            onClick = { viewModel.launchPermissionSettings(activity) },
        ) {
            Text(stringResource(R.string.settings_label))
        }
    }
}
