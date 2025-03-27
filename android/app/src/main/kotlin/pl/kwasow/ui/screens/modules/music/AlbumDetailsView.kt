package pl.kwasow.ui.screens.modules.music

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.FlamingoTopAppBar
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailsView(albumUuid: String) {
    val viewModel = koinViewModel<MusicModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    val album = viewModel.getAlbumByUuid(albumUuid)

    if (album == null) {
        AlbumNotFound(uuid = albumUuid)
        return
    }

    Scaffold(
        topBar = {
            FlamingoTopAppBar(
                title = album.title,
                onBackPressed = navigation.navigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AlbumDetailsHeader(album = album)
            AlbumTrackList(album = album)
        }
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumNotFound(uuid: String) {
    val navigation = LocalFlamingoNavigation.current

    Scaffold(
        topBar = {
            FlamingoTopAppBar(
                title = stringResource(R.string.error),
                onBackPressed = navigation.navigateBack,
            )
        },
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
        ) {
            SelectionContainer {
                Text(
                    stringResource(id = R.string.module_music_unknown_album_uuid, uuid),
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
