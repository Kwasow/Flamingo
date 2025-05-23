package pl.kwasow.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.FlamingoBackgroundLight
import pl.kwasow.ui.components.PermissionNotificationView
import pl.kwasow.ui.composition.LocalBottomBarHeight
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val bottomBarHeight = LocalBottomBarHeight.current
    val navigation = LocalFlamingoNavigation.current

    LaunchedEffect(true) {
        viewModel.doLaunchTasks(navigation.navigateToLogin)
    }

    PermissionNotificationView {
        Scaffold(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
        ) { paddingValues ->
            FlamingoBackgroundLight()

            Column(
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                        .padding(bottom = bottomBarHeight),
            ) {
                TopBar(navigateToSettings = navigation.navigateToSettings)
                WidgetsView()
                ModuleList()
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
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.contentDescription_karonia_logo),
            contentScale = ContentScale.FillHeight,
            modifier =
                Modifier
                    .height(40.dp)
                    .padding(start = 24.dp, bottom = 4.dp),
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
