package pl.kwasow.ui.widgets.music

import androidx.lifecycle.ViewModel
import pl.kwasow.managers.PlaybackManager

class PlaybackWidgetViewModel(
    private val playbackManager: PlaybackManager,
) : ViewModel() {
    // ====== Fields
    var currentTrack = playbackManager.currentTrack
    var isPlaying = playbackManager.isPlaybackActive
    var isLoading = playbackManager.isLoading

    // ====== Public methods
    fun play() = playbackManager.play()

    fun pause() = playbackManager.pause()

    fun stop() = playbackManager.stop()

    fun next() = playbackManager.next()

    fun previous() = playbackManager.previous()
}
