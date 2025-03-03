package pl.kwasow.sunshine.ui.widgets.music

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.sunshine.R
import pl.kwasow.sunshine.ui.components.LoadingView
import pl.kwasow.sunshine.ui.components.SimplePhotoView

// ====== Public composables
@Composable
fun PlaybackControls() {
    val viewModel = koinViewModel<PlaybackWidgetViewModel>()
    val currentTrack = viewModel.currentTrack.observeAsState()

    currentTrack.value?.let {
        PlaybackControls(
            title =
                it.mediaMetadata.title
                    ?: stringResource(R.string.widget_nowplaying_no_title),
            artist =
                it.mediaMetadata.artist
                    ?: stringResource(R.string.widget_nowplaying_no_artist),
            cover = it.mediaMetadata.artworkUri,
        )
    }
}

// ====== Private composables
@Composable
private fun PlaybackControls(
    title: CharSequence,
    artist: CharSequence,
    cover: Uri?,
) {
    val viewModel = koinViewModel<PlaybackWidgetViewModel>()

    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = { newValue ->
                if (
                    newValue == SwipeToDismissBoxValue.StartToEnd ||
                    newValue == SwipeToDismissBoxValue.EndToStart
                ) {
                    viewModel.stop()
                }

                // We always want to reset the state back to it's initial value
                return@rememberSwipeToDismissBoxState false
            },
            positionalThreshold = with(LocalDensity.current) { { 150.dp.toPx() } },
        )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {},
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Metadata(
                    title = title,
                    artist = artist,
                    cover = cover,
                    rowScope = this,
                )
                ButtonControls()
            }
        }
    }
}

@Composable
private fun Metadata(
    title: CharSequence,
    artist: CharSequence,
    cover: Uri?,
    rowScope: RowScope,
) {
    with(rowScope) {
        Card(modifier = Modifier.padding(end = 8.dp)) {
            SimplePhotoView(
                uri = cover.toString(),
                contentDescription = stringResource(id = R.string.contentDescription_album_cover),
                modifier =
                    Modifier
                        .height(48.dp)
                        .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = artist.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ButtonControls(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<PlaybackWidgetViewModel>()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { viewModel.previous() }) {
            Icon(
                painterResource(id = R.drawable.ic_previous),
                contentDescription =
                    stringResource(
                        id = R.string.contentDescription_previous_track_icon,
                    ),
            )
        }

        PlayPauseButton()

        IconButton(onClick = { viewModel.next() }) {
            Icon(
                painterResource(id = R.drawable.ic_next),
                contentDescription =
                    stringResource(
                        id = R.string.contentDescription_next_track_icon,
                    ),
            )
        }
    }
}

@Composable
private fun PlayPauseButton() {
    val viewModel = koinViewModel<PlaybackWidgetViewModel>()
    val isPlaying = viewModel.isPlaying.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState()

    Box(
        modifier = Modifier.size(36.dp),
    ) {
        if (isLoading.value == true) {
            LoadingView(
                modifier = Modifier.fillMaxSize(),
            )
        } else if (isPlaying.value == true) {
            IconButton(onClick = { viewModel.pause() }) {
                Icon(
                    painterResource(id = R.drawable.ic_pause),
                    contentDescription = stringResource(id = R.string.contentDescription_pause),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        } else {
            IconButton(onClick = { viewModel.play() }) {
                Icon(
                    painterResource(id = R.drawable.ic_play),
                    contentDescription = stringResource(id = R.string.contentDescription_play),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun PlaybackControlsPreview() {
    PlaybackControls(
        title = "Title",
        artist = "Artist",
        cover = null,
    )
}
