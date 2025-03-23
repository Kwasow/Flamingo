package pl.kwasow.ui.screens.modules.music

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kwasow.data.types.Album
import pl.kwasow.data.types.AudioTrack
import pl.kwasow.managers.AudioManager
import pl.kwasow.managers.PlaybackManager

class MusicModuleViewModel(
    private val audioManager: AudioManager,
    private val playbackManager: PlaybackManager,
) : ViewModel() {
    // ====== Fields
    var albumList: List<Album> by mutableStateOf(emptyList())
        private set
    var isAlbumListRefreshing: Boolean by mutableStateOf(false)
        private set
    var albumListLoaded: Boolean by mutableStateOf(false)
        private set

    var currentTrack = playbackManager.currentTrack
    var isPlaying = playbackManager.isPlaybackActive

    // ====== Public methods
    fun refreshAlbumList(force: Boolean = false) {
        viewModelScope.launch {
            isAlbumListRefreshing = true
            albumList = audioManager.getAlbums(force)
            isAlbumListRefreshing = false
            albumListLoaded = true
        }
    }

    // Album management
    fun getAlbumByUuid(uuid: String): Album? = audioManager.getAlbum(uuid)

    fun getTrackId(track: AudioTrack): String = audioManager.getTrackId(track)

    fun checkAlbumDownloaded(album: Album): Boolean = audioManager.isAlbumDownloaded(album)

    fun getAlbumCoverUri(album: Album): Uri = audioManager.getAlbumCoverUri(album)

    fun downloadAlbum(album: Album) = audioManager.downloadAlbum(album)

    fun removeAlbum(album: Album) = audioManager.removeAlbum(album)

    // Playback management
    fun playAlbum(
        album: Album,
        startingIndex: Int = 0,
    ) = playbackManager.playAlbum(album, startingIndex)
}
