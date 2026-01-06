package pl.kwasow.ui.screens.modules.music.modals

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
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.DeleteDialog
import pl.kwasow.ui.screens.modules.music.MusicModuleViewModel

// ====== Public composables
@Composable
fun DialogMusicAlbumDelete(
    isShowing: Boolean,
    albumName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel = koinViewModel<MusicModuleViewModel>()

    if (isShowing) {
        DeleteDialog(
            title = stringResource(id = R.string.module_music_delete_dialog_header),
            content = albumName,
            isEnabled = TODO(),
            isError = TODO(),
            onConfirm = onConfirm,
            onCancel = onCancel,
        )
    }
}
