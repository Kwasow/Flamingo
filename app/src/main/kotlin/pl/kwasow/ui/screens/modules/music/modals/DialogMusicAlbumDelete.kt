package pl.kwasow.ui.screens.modules.music.modals

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.DeleteDialog
import pl.kwasow.ui.screens.modules.music.MusicModuleViewModel

// ====== Public composables
@Composable
fun DialogMusicAlbumDelete() {
    val viewModel = koinViewModel<MusicModuleViewModel>()
    val album = viewModel.albumToDeleteFromDownloads

    if (album != null) {
        DeleteDialog(
            title = stringResource(id = R.string.module_music_delete_dialog_header),
            content = album.title,
            onConfirm = { viewModel.deleteAlbumFromDownloads(album) },
            onCancel = { viewModel.closeDeleteAlbumFromDownloadsDialog() },
        )
    }
}
