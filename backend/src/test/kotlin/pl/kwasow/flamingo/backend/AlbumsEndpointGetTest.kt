package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.music.AlbumsGetResponse
import kotlin.collections.map
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class AlbumsEndpointGetTest : BaseTest() {
    @Test
    fun `couple members albums match`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/albums/get")))
                .andExpect(status().isOk)
                .andReturn()
        val bobResult =
            mockMvc
                .perform(requestBob(get("/albums/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceAlbums =
            json
                .decodeFromString<AlbumsGetResponse>(aliceResult.response.contentAsString)
        val bobAlbums =
            json
                .decodeFromString<AlbumsGetResponse>(bobResult.response.contentAsString)

        assertEquals(bobAlbums, aliceAlbums)
    }

    @Test
    fun `intersection between different couples is empty`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/albums/get")))
                .andExpect(status().isOk)
                .andReturn()
        val malloryResult =
            mockMvc
                .perform(requestMallory(get("/albums/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceAlbums =
            json
                .decodeFromString<AlbumsGetResponse>(aliceResult.response.contentAsString)
        val aliceAlbumUuids = aliceAlbums.map { it.uuid }.toSet()
        val aliceTracks = aliceAlbums.map { it.tracks }.flatten().toSet()
        val malloryAlbums =
            json
                .decodeFromString<AlbumsGetResponse>(malloryResult.response.contentAsString)
        val malloryAlbumsUuids = malloryAlbums.map { it.uuid }.toSet()
        val malloryTracks = malloryAlbums.map { it.tracks }.flatten().toSet()

        assertEquals(0, aliceAlbumUuids.intersect(malloryAlbumsUuids).size)
        assertEquals(0, aliceTracks.intersect(malloryTracks).size)
    }

    @Test
    fun `albums response matches expected format`() {
        val request = requestBob(get("/albums/get"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val albums =
            json
                .decodeFromString<AlbumsGetResponse>(result.response.contentAsString)

        assertEquals(1, albums.size)
        assertEquals(2, albums[0].tracks.size)
        assert(albums[0].uuid.startsWith("alicebob"))
        assert(albums[0].coverName.startsWith("alicebob"))

        for (track in albums[0].tracks) {
            assert(track.albumUuid == albums[0].uuid)
            assert(track.resourceName.startsWith("alicebob"))
        }
    }
}
