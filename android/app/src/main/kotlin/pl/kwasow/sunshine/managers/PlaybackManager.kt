package pl.kwasow.sunshine.managers

import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import pl.kwasow.sunshine.data.Album

interface PlaybackManager {
    // ====== Fields
    val currentTrack: LiveData<MediaItem?>

    val isPlaybackActive: LiveData<Boolean>

    val isLoading: LiveData<Boolean>

    // ====== Methods
    fun playAlbum(
        album: Album,
        startingIndex: Int = 0,
    )

    fun play()

    fun pause()

    fun stop()

    fun next()

    fun previous()
}
