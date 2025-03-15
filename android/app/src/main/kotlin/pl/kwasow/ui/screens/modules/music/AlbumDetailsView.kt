package pl.kwasow.ui.screens.modules.music

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val album =
        viewModel.getAlbumByUuid(albumUuid)
            ?: return albumNotFound(navigation.navigateBack, LocalContext.current)

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
private fun albumNotFound(
    onBackPressed: () -> Unit,
    context: Context,
) {
    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
    onBackPressed()
}
