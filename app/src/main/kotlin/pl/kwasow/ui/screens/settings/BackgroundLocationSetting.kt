package pl.kwasow.ui.screens.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R

// ====== Public composables
@Composable
fun BackgroundLocationEntry() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val activity = LocalActivity.current
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val allowLocationRequests by viewModel.allowLocationRequests.collectAsState(false)
    var showDialog by remember { mutableStateOf(false) }

    val onToggle = {
        viewModel.toggleAllowLocationRequests(onPermissionMissing = { showDialog = true })
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            viewModel.updateAllowLocationRequestState(showDialog)
            showDialog = false
        }
    }

    SettingsEntry(
        icon = Icons.Outlined.LocationOn,
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
                checked = allowLocationRequests,
                onCheckedChange = { onToggle() },
            )
        }
    }

    BackgroundLocationDialog(
        isShowing = showDialog,
        partnerName = viewModel.partnerName,
        onConfirm = { viewModel.launchPermissionSettings(activity) },
        onCancel = { showDialog = false },
    )
}

// ====== Private composables
@Composable
private fun BackgroundLocationDialog(
    isShowing: Boolean,
    partnerName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    if (!isShowing) {
        return
    }

    AlertDialog(
        icon = { AlertIcon() },
        title = { AlertTitle() },
        text = { AlertContent(partnerName = partnerName) },
        confirmButton = { AlertConfirmButton(onClick = onConfirm) },
        dismissButton = { AlertCancelButton(onClick = onCancel) },
        onDismissRequest = { onCancel() },
    )
}

@Composable
private fun AlertIcon() {
    Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = stringResource(id = R.string.contentDescription_location),
    )
}

@Composable
private fun AlertTitle() {
    Text(
        text = stringResource(id = R.string.settings_location_dialog_title),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertConfirmButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.ok))
    }
}

@Composable
private fun AlertCancelButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.cancel))
    }
}

@Composable
private fun AlertContent(partnerName: String) {
    Text(
        text =
            stringResource(
                id = R.string.settings_location_dialog_content,
                partnerName,
            ),
        textAlign = TextAlign.Center,
    )
}

// ====== Previews
@Preview
@Composable
private fun BackgroundLocationDialogPreview() {
    BackgroundLocationDialog(
        isShowing = true,
        partnerName = "Anon",
        onConfirm = {},
        onCancel = {},
    )
}
