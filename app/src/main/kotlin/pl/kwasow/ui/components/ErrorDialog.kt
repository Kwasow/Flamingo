package pl.kwasow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import pl.kwasow.R

// ====== Public composables
@Composable
fun ErrorDialog(
    title: String,
    error: String,
    isShowing: Boolean,
    clearError: () -> Unit,
) {
    if (!isShowing) {
        return
    }

    AlertDialog(
        icon = { AlertIcon() },
        title = { AlertTitle(title = title) },
        text = { AlertContent(error) },
        confirmButton = { AlertConfirmButton(onClick = clearError) },
        onDismissRequest = { clearError() },
    )
}

// ====== Private composables
@Composable
private fun AlertIcon() {
    Icon(
        imageVector = Icons.Outlined.Warning,
        contentDescription = stringResource(id = R.string.contentDescription_warning),
    )
}

@Composable
private fun AlertTitle(title: String) {
    Text(
        text = title,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertContent(error: String) {
    Text(text = error)
}

@Composable
private fun AlertConfirmButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.ok))
    }
}

// ====== Previews
@Preview
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog(
        title = "Error name",
        error = "Something went wrong",
        isShowing = true,
        clearError = {},
    )
}
