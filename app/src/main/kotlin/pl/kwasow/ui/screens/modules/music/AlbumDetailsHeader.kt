package pl.kwasow.ui.screens.modules.music

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.ui.components.PhotoView

// ====== Public composables
@Composable
fun AlbumDetailsHeader(album: Album) {
    val viewModel = koinViewModel<MusicModuleViewModel>()

    var downloaded by remember { mutableStateOf(viewModel.checkAlbumDownloaded(album)) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlbumCover(uri = viewModel.getAlbumCoverUri(album))

        Column {
            AlbumMetadata(album = album)

            AlbumActions(
                album = album,
                downloaded = downloaded,
                showRemoveDialog = { showRemoveDialog = true },
                downloadAlbum = {
                    viewModel.downloadAlbum(album)
                    downloaded = !downloaded
                },
            )
        }
    }

    DeleteAlbumDialog(
        isShowing = showRemoveDialog,
        albumName = album.title,
        onConfirm = {
            viewModel.removeAlbum(album)
            downloaded = false
            showRemoveDialog = false
        },
        onCancel = { showRemoveDialog = false },
    )
}

// ====== Private composables
@Composable
private fun AlbumCover(uri: Uri) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth(0.5f)
                .padding(end = 16.dp)
                .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        PhotoView(
            uri = uri.toString(),
            contentDescription = stringResource(id = R.string.contentDescription_album_cover),
            contentScale = ContentScale.Crop,
            clickable = false,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun AlbumMetadata(album: Album) {
    Text(
        text = album.title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = album.artist,
        style = MaterialTheme.typography.labelMedium,
        color = Color.LightGray,
    )
}

@Composable
private fun AlbumActions(
    album: Album,
    downloaded: Boolean,
    showRemoveDialog: () -> Unit,
    downloadAlbum: (Album) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
    ) {
        IconButton(
            colors = IconButtonDefaults.filledTonalIconButtonColors(),
            onClick = {
                if (downloaded) {
                    showRemoveDialog()
                } else {
                    downloadAlbum(album)
                }
            },
        ) {
            Icon(
                painter =
                    if (downloaded) {
                        painterResource(id = R.drawable.ic_download_done)
                    } else {
                        painterResource(id = R.drawable.ic_download)
                    },
                contentDescription = stringResource(id = R.string.contentDescription_download_icon),
            )
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun AlbumDetailsHeaderPreview() {
    val album =
        Album(
            id = 0,
            uuid = "UUID-0",
            title = "Title",
            artist = "Artist",
            coverName = "UUID-1",
            coupleId = 1,
            tracks = mutableListOf(),
        )

    AlbumDetailsHeader(album = album)
}
