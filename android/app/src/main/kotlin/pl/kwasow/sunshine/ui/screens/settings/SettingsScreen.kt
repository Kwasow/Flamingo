package pl.kwasow.sunshine.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.sunshine.BuildConfig
import pl.kwasow.sunshine.R
import pl.kwasow.sunshine.ui.components.SunshineTopAppBar

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        topBar = {
            SunshineTopAppBar(
                onBackPressed = onBackPressed,
                title = stringResource(id = R.string.settings_label),
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues),
        ) {
            AppDetails()
            InfoSettingsSection()
            GeneralSettingsSection(onLogout = onLogout)
        }
    }
}

// ====== Private composables
@Composable
private fun AppDetails() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(
                        top = 32.dp,
                        start = 12.dp,
                        bottom = 12.dp,
                        end = 12.dp,
                    )
                    .aspectRatio(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.contentDescription_karonia_logo),
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(id = R.string.settings_app_version, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
        )
    }
}

@Composable
private fun GeneralSettingsSection(onLogout: () -> Unit) {
    val viewModel = koinViewModel<SettingsScreenViewModel>()

    SettingsSection(title = stringResource(id = R.string.settings_general)) {
        BackgroundLocationEntry()

        HorizontalDivider()

        SettingsEntry(
            icon = rememberVectorPainter(image = Icons.Outlined.Delete),
            name = stringResource(id = R.string.settings_clear_cache),
            description = stringResource(id = R.string.settings_clear_cache_description),
            onClick = { viewModel.freeUpMemory() },
        )

        HorizontalDivider()

        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_logout),
            iconTint = MaterialTheme.colorScheme.error,
            name = stringResource(id = R.string.settings_logout),
            description = stringResource(id = R.string.settings_logout_description),
            onClick = { viewModel.signOut(onLogout) },
        )
    }
}

@Composable
private fun BackgroundLocationEntry() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val allowLocationRequests by viewModel.allowLocationRequests.observeAsState()
    var showDialog by remember { mutableStateOf(false) }

    val onToggle = {
        viewModel.toggleAllowLocationRequests { showDialog = true }
    }

    SettingsEntry(
        icon = rememberVectorPainter(image = Icons.Outlined.LocationOn),
        name = stringResource(id = R.string.settings_location_sharing),
        description = stringResource(id = R.string.settings_location_sharing_description),
        onClick = onToggle,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp,
                ),
        ) {
            VerticalDivider(modifier = Modifier.padding(end = 12.dp))

            Switch(
                checked = allowLocationRequests == true,
                onCheckedChange = { onToggle() },
            )
        }
    }

    BackgroundLocationDialog(isShowing = showDialog)
}

@Composable
private fun BackgroundLocationDialog(isShowing: Boolean) {

}

@Composable
private fun InfoSettingsSection() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()

    SettingsSection {
        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_store),
            name = stringResource(id = R.string.settings_play_store),
            description = stringResource(id = R.string.settings_play_store_description),
            onClick = { viewModel.launchStore() },
        )
    }
}

// ====== Previews
@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        onBackPressed = {},
        onLogout = {},
    )
}
