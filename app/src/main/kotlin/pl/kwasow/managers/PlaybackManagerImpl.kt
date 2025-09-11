package pl.kwasow.managers

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.services.PlaybackService

class PlaybackManagerImpl(
    private val context: Context,
    private val audioManager: AudioManager,
) : PlaybackManager {
    // ====== Fields
    private var sessionToken: SessionToken? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    override val currentTrack = MutableLiveData<MediaItem?>(null)
    override val isPlaybackActive = MutableLiveData(false)
    override val isLoading = MutableLiveData(true)

    // ====== Public methods
    override fun playAlbum(
        album: Album,
        startingIndex: Int,
    ) {
        initMediaSession(context)
        val coverUri = audioManager.getAlbumCoverUri(album)

        val mediaItems =
            album.tracks.map { track ->
                val trackUri = audioManager.getTrackUri(track)
                val trackId = audioManager.getTrackId(track)

                val metadataBuilder =
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(album.artist)
                        .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
                        .setArtworkUri(coverUri)

                val mediaItemBuilder =
                    MediaItem.Builder()
                        .setUri(trackUri)
                        .setMediaId(trackId)
                        .setMediaMetadata(metadataBuilder.build())

                return@map mediaItemBuilder.build()
            }

        controllerFuture?.addListener({
            val mediaController = controllerFuture?.get() ?: return@addListener
            mediaController.setMediaItems(mediaItems, startingIndex, 0)
            mediaController.play()
        }, MoreExecutors.directExecutor())
    }

    override fun play() {
        controllerFuture?.addListener({
            controllerFuture?.get()?.play()
        }, MoreExecutors.directExecutor())
    }

    override fun pause() {
        controllerFuture?.addListener({
            controllerFuture?.get()?.pause()
        }, MoreExecutors.directExecutor())
    }

    override fun stop() {
        controllerFuture?.addListener({
            controllerFuture?.get()?.stop()
        }, MoreExecutors.directExecutor())
    }

    override fun next() {
        controllerFuture?.addListener({
            controllerFuture?.get()?.seekToNext()
        }, MoreExecutors.directExecutor())
    }

    override fun previous() {
        controllerFuture?.addListener({
            controllerFuture?.get()?.seekToPrevious()
        }, MoreExecutors.directExecutor())
    }

    // ====== Private methods
    private fun initMediaSession(context: Context) {
        synchronized(this) {
            if (sessionToken == null && controllerFuture == null) {
                val newSessionToken =
                    SessionToken(context, ComponentName(context, PlaybackService::class.java))
                sessionToken = newSessionToken

                controllerFuture = MediaController.Builder(context, newSessionToken).buildAsync()
                controllerFuture?.addListener({
                    val mediaController = controllerFuture?.get() ?: return@addListener

                    mediaController.addListener(
                        object : Player.Listener {
                            override fun onMediaItemTransition(
                                mediaItem: MediaItem?,
                                reason: Int,
                            ) {
                                currentTrack.postValue(mediaItem)
                            }

                            override fun onPlaybackStateChanged(playbackState: Int) {
                                if (
                                    playbackState == Player.STATE_ENDED ||
                                    playbackState == Player.STATE_IDLE
                                ) {
                                    isPlaybackActive.postValue(false)
                                    isLoading.postValue(true)
                                    currentTrack.postValue(null)
                                }

                                if (playbackState == Player.STATE_BUFFERING) {
                                    isLoading.postValue(true)
                                } else if (playbackState == Player.STATE_READY) {
                                    isLoading.postValue(false)
                                }
                            }

                            override fun onIsPlayingChanged(isPlaying: Boolean) {
                                isPlaybackActive.postValue(isPlaying)
                            }
                        },
                    )
                }, MoreExecutors.directExecutor())
            }
        }
    }
}
