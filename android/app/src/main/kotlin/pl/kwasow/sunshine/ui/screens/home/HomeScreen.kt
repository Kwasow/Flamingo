package pl.kwasow.sunshine.ui.screens.home

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.sunshine.R
import pl.kwasow.sunshine.ui.components.FlamingoBackground
import pl.kwasow.sunshine.ui.composition.LocalSunshineNavigation

// ====== Public composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val navigation = LocalSunshineNavigation.current

    LaunchedEffect(true) {
        viewModel.doLaunchTasks(navigation.navigateToLogin)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = viewModel.rememberNotificationPermissionState()

        LaunchedEffect(true) {
            notificationPermission.launchPermissionRequest()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            FlamingoBackground()

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                TopBar(navigateToSettings = navigation.navigateToSettings)
                WidgetsView()
                ModuleList(navigationBarPadding = paddingValues.calculateBottomPadding())
            }
        }
    }
}

// ====== Private composables
@Composable
private fun TopBar(navigateToSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.karonia),
            contentDescription = stringResource(id = R.string.contentDescription_karonia_logo),
            modifier =
                Modifier
                    .fillMaxWidth(0.45f)
                    .padding(vertical = 12.dp)
                    .padding(start = 24.dp),
        )

        IconButton(onClick = { navigateToSettings() }) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = stringResource(id = R.string.contentDescription_settings_icon),
                modifier = Modifier.padding(end = 16.dp),
                tint = Color.DarkGray,
            )
        }
    }
}

// ====== Previews
@Composable
@Preview
private fun HomeScreenPreview() {
    HomeScreen()
}
