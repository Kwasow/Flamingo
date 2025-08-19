package pl.kwasow.managers

import android.net.Uri
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.music.AudioTrack

interface AudioManager {
    // ====== Methods
    suspend fun getAlbums(forceRefresh: Boolean = false): List<Album>

    fun getAlbum(uuid: String): Album?

    fun getAlbumCoverUri(album: Album): Uri

    fun getTrackUri(track: AudioTrack): Uri

    fun getTrackId(track: AudioTrack): String

    fun isAlbumDownloaded(album: Album): Boolean

    fun downloadAlbum(album: Album)

    fun removeAlbum(album: Album)

    fun removeAllDownloads()
}
