package pl.kwasow.ui.screens.modules.music

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
fun DeleteAlbumDialog(
    isShowing: Boolean,
    albumName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    if (!isShowing) {
        return
    }

    AlertDialog(
        title = { AlertTitle() },
        icon = { AlertIcon() },
        text = { AlertContent(albumName = albumName) },
        confirmButton = { AlertConfirmButton(onConfirm = onConfirm) },
        dismissButton = { AlertDismissButton(onCancel = onCancel) },
        onDismissRequest = { onCancel() },
    )
}

// ====== Private composables
@Composable
private fun AlertTitle() {
    Text(
        text = stringResource(id = R.string.module_music_delete_dialog_header),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertIcon() {
    Icon(
        Icons.Outlined.Delete,
        contentDescription = stringResource(id = R.string.contentDescription_trash_icon),
    )
}

@Composable
private fun AlertContent(albumName: String) {
    Text(
        text =
            stringResource(
                id = R.string.module_music_delete_dialog_text,
                albumName,
            ),
    )
}

@Composable
private fun AlertConfirmButton(onConfirm: () -> Unit) {
    TextButton(onClick = { onConfirm() }) {
        Text(text = stringResource(id = R.string.confirm))
    }
}

@Composable
private fun AlertDismissButton(onCancel: () -> Unit) {
    TextButton(onClick = { onCancel() }) {
        Text(text = stringResource(id = R.string.cancel))
    }
}

// ====== Previews
@Preview
@Composable
private fun RemoveAlbumDialogPreview() {
    DeleteAlbumDialog(
        isShowing = true,
        albumName = "Album name",
        onConfirm = {},
        onCancel = {},
    )
}
