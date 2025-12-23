package pl.kwasow.ui.screens.modules.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.ui.components.PhotoView
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@Composable
fun AlbumListView(albums: List<Album>) {
    if (albums.isEmpty()) {
        NoAlbumsView()
        return
    }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
    ) {
        itemsIndexed(albums) { _, album ->
            AlbumView(album = album)
        }
    }
}

// ====== Private composables
@Composable
private fun AlbumView(album: Album) {
    val viewModel = koinViewModel<MusicModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                .aspectRatio(2f / 1f),
        onClick = { navigation.navigateToMusicAlbum(album.uuid) },
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
        ) {
            PhotoView(
                modifier = Modifier.fillMaxWidth(),
                uri = viewModel.getAlbumCoverUri(album).toString(),
                contentDescription = stringResource(id = R.string.contentDescription_album_cover),
                contentScale = ContentScale.Crop,
                clickable = false,
            )

            AlbumDetailsPhotoOverlay(album)
        }
    }
}

@Composable
private fun AlbumDetailsPhotoOverlay(album: Album) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color.Transparent,
                                    Color.DarkGray,
                                ),
                        ),
                ).padding(12.dp),
    ) {
        Text(
            text = album.title,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            text = album.artist,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 16.sp,
            color = Color.LightGray,
        )
    }
}

@Composable
private fun NoAlbumsView() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.module_music_no_albums),
            style = MaterialTheme.typography.titleMedium,
            color = Color.LightGray,
        )
    }
}

// ====== Previews
@Preview
@Composable
private fun AlbumListViewPreviewEmpty() {
    AlbumListView(albums = emptyList())
}
