package pl.kwasow.managers

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.kwasow.BuildConfig
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.music.AudioTrack
import pl.kwasow.services.FlamingoDownloadService
import java.io.File
import java.net.URL

class AudioManagerImpl(
    private val context: Context,
    private val requestManager: RequestManager,
) : AudioManager {
    // ====== Fields
    companion object {
        private const val ALBUMS_PATH = "api/resources/albums"
        private const val ALBUMS_URL = "${BuildConfig.BASE_URL}/$ALBUMS_PATH"
        private const val ALBUM_INFO_FILE_NAME = "album.json"
    }

    private val json = Json { ignoreUnknownKeys = true }
    private var albumsCache: List<Album>? = null

    // ====== Interface methods
    override suspend fun getAlbums(forceRefresh: Boolean): List<Album> {
        if (forceRefresh) {
            albumsCache = null
        }

        val localAlbums = fetchAlbumsLocal()
        val remoteAlbums = albumsCache ?: requestManager.getAlbums() ?: emptyList()

        val remoteAlbumsFiltered =
            remoteAlbums.filterNot { remote ->
                localAlbums.any { local -> local.uuid == remote.uuid }
            }

        val albums = (localAlbums + remoteAlbumsFiltered).sortedBy { it.id }
        albumsCache = albums

        return albums
    }

    override fun getAlbum(uuid: String): Album? = albumsCache?.find { it.uuid == uuid }

    override fun getAlbumCoverUri(album: Album): Uri {
        val localFile = getLocalAlbumResource(album.uuid, album.coverName)
        val remoteUrl = "$ALBUMS_URL/${album.uuid}/${album.coverName}"

        return localFile?.toUri()
            ?: Uri.parse(remoteUrl)
    }

    override fun getTrackUri(track: AudioTrack): Uri =
        Uri.parse(
            "$ALBUMS_URL/${track.albumUuid}/${track.resourceName}",
        )

    override fun getTrackId(track: AudioTrack): String = "${track.albumUuid}/${track.resourceName}"

    override fun isAlbumDownloaded(album: Album): Boolean {
        val albumDirectory = File(context.filesDir, "$ALBUMS_PATH/${album.uuid}")

        val infoFile = File(albumDirectory, ALBUM_INFO_FILE_NAME)
        val coverFile = File(albumDirectory, album.coverName)

        return infoFile.exists() && coverFile.exists()
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun downloadAlbum(album: Album) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadAlbumDetails(album)

            album.tracks.forEach { track ->
                downloadTrack(track)
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun removeAlbum(album: Album) {
        val albumDirectory = File(context.filesDir, "$ALBUMS_PATH/${album.uuid}")
        albumDirectory.deleteRecursively()

        album.tracks.forEach { track ->
            DownloadService.sendRemoveDownload(
                context,
                FlamingoDownloadService::class.java,
                getTrackId(track),
                false,
            )
        }
    }

    @OptIn(UnstableApi::class)
    override fun removeAllDownloads() {
        DownloadService.sendRemoveAllDownloads(
            context,
            FlamingoDownloadService::class.java,
            false,
        )

        val albumsDirectory = File(context.filesDir, ALBUMS_PATH)
        if (albumsDirectory.exists()) {
            albumsDirectory.deleteRecursively()
        }
    }

    // ====== Private methods
    private fun fetchAlbumsLocal(): List<Album> {
        val audioFolder = File(context.filesDir, ALBUMS_PATH)
        val albums = mutableListOf<Album>()

        if (!audioFolder.exists()) {
            return emptyList()
        }

        audioFolder.listFiles()?.forEach {
            if (it.isDirectory) {
                val infoFile = File(it, ALBUM_INFO_FILE_NAME)
                val jsonContents = String(infoFile.readBytes())

                try {
                    val album = json.decodeFromString<Album>(jsonContents)
                    albums.add(album)
                } catch (e: Exception) {
                    // Remove corrupted downloads
                    it.deleteRecursively()
                }
            }
        }

        return albums
    }

    private fun getLocalAlbumResource(
        albumUuid: String,
        uuid: String,
    ): File? {
        val file = File(context.filesDir, "$ALBUMS_PATH/$albumUuid/$uuid")
        return if (file.exists()) file else null
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun downloadTrack(track: AudioTrack) {
        val downloadRequest =
            DownloadRequest.Builder(
                getTrackId(track),
                getTrackUri(track),
            ).build()

        DownloadService.sendAddDownload(
            context,
            FlamingoDownloadService::class.java,
            downloadRequest,
            false,
        )
    }

    private fun downloadAlbumDetails(album: Album) {
        val folder = File(context.filesDir, "$ALBUMS_PATH/${album.uuid}")
        val infoFile = File(folder, ALBUM_INFO_FILE_NAME)
        val coverFile = File(folder, album.coverName)
        folder.mkdirs()

        infoFile.writeText(json.encodeToString(album))

        if (!coverFile.exists()) {
            val coverUrl = URL(getAlbumCoverUri(album).toString())
            coverFile.writeBytes(coverUrl.readBytes())
        }
    }
}
