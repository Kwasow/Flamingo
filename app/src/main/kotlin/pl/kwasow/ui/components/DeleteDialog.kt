package pl.kwasow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R

// ====== Public composables
@Composable
fun DeleteDialog(
    title: String,
    content: String,
    isEnabled: Boolean,
    isError: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        icon = { AlertIcon() },
        title = { AlertTitle(title = title) },
        text = {
            AlertContent(
                content = content,
                isError = isError,
            )
        },
        confirmButton = {
            AlertConfirmButton(
                enabled = isEnabled,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            AlertDismissButton(
                enabled = isEnabled,
                onClick = onCancel,
            )
        },
        onDismissRequest = onCancel,
    )
}

// ====== Private composables
@Composable
private fun AlertIcon() {
    Icon(
        imageVector = Icons.Outlined.Delete,
        contentDescription = stringResource(id = R.string.contentDescription_trash_icon),
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
private fun AlertContent(
    content: String,
    isError: Boolean,
) {
    Column {
        Text(
            text = stringResource(id = R.string.delete_item, content),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = stringResource(id = R.string.delete_failed),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.delete))
    }
}

@Composable
private fun AlertDismissButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.cancel))
    }
}

// ====== Previews
@Preview
@Composable
private fun DeleteDialogPreview() {
    DeleteDialog(
        title = stringResource(id = R.string.module_wishlist_delete_dialog_header),
        content = "I'd like a new car",
        isEnabled = true,
        isError = false,
        onConfirm = {},
        onCancel = {},
    )
}

@Preview
@Composable
private fun DeleteDialogButtonsDisabledPreview() {
    DeleteDialog(
        title = stringResource(id = R.string.module_memories_delete_dialog_header),
        content = "We went on a boat trip",
        isEnabled = false,
        isError = false,
        onConfirm = {},
        onCancel = {},
    )
}

@Preview
@Composable
private fun DeleteDialogErrorPreview() {
    DeleteDialog(
        title = stringResource(id = R.string.module_memories_delete_dialog_header),
        content = "We went on a boat trip",
        isEnabled = false,
        isError = true,
        onConfirm = {},
        onCancel = {},
    )
}
