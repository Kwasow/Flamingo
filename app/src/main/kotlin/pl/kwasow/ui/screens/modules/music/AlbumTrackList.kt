package pl.kwasow.ui.screens.modules.music

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.music.AudioTrack

// ====== Public composables
@Composable
fun AlbumTrackList(album: Album) {
    val viewModel = koinViewModel<MusicModuleViewModel>()
    val color = MaterialTheme.colorScheme.onPrimary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(color),
    ) {
        album.tracks.forEachIndexed { index, track ->
            TrackListItem(
                track = track,
                index = index,
                onClick = { viewModel.playAlbum(album, index) },
            )

            if (index != album.tracks.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

// ====== Private composables
@Composable
private fun TrackListItem(
    track: AudioTrack,
    index: Int,
    onClick: () -> Unit,
) {
    val viewModel = koinViewModel<MusicModuleViewModel>()

    val currentTrack = viewModel.currentTrack.observeAsState()
    val isPlaying = viewModel.isPlaying.observeAsState()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (
            currentTrack.value?.mediaId == viewModel.getTrackId(track) &&
            isPlaying.value == true
        ) {
            NowPlayingTrackIndicator()
        } else {
            Text(
                text = "${index + 1}",
                modifier = Modifier.fillMaxWidth(0.15f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
            )
        }

        TrackName(
            title = track.title,
            comment = track.comment,
        )
    }
}

@Composable
private fun NowPlayingTrackIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_disc_rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec =
            infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
            ),
        label = "disk_rotation",
    )

    Icon(
        painter = painterResource(id = R.drawable.ic_disc),
        contentDescription = stringResource(id = R.string.contentDescription_disc_icon),
        modifier =
            Modifier
                .fillMaxWidth(0.15f)
                .graphicsLayer {
                    rotationZ = angle
                },
    )
}

@Composable
private fun TrackName(
    title: String,
    comment: String?,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        comment?.let { comment ->
            Text(
                text = comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun TrackListItemCommentPreview() {
    val track =
        AudioTrack(
            id = 1,
            title = "Track title",
            comment = "Subtitle",
            resourceName = "UUID-1",
            albumUuid = "UUID-0",
        )

    TrackListItem(
        track = track,
        index = 0,
        onClick = {},
    )
}

@Preview
@Composable
private fun TrackListItemNoCommentPreview() {
    val track =
        AudioTrack(
            id = 1,
            title = "Track title",
            comment = null,
            resourceName = "UUID-1",
            albumUuid = "UUID-0",
        )

    TrackListItem(
        track = track,
        index = 0,
        onClick = {},
    )
}

@Preview
@Composable
private fun AlbumTrackListPreview() {
    val track =
        AudioTrack(
            id = 1,
            title = "Track title",
            comment = "Subtitle",
            resourceName = "UUID-1",
            albumUuid = "UUID-0",
        )

    val album =
        Album(
            id = 0,
            uuid = "UUID-0",
            title = "Title",
            artist = "Artist",
            coverName = "UUID-1",
            tracks = listOf(track, track, track),
        )

    AlbumTrackList(album)
}
