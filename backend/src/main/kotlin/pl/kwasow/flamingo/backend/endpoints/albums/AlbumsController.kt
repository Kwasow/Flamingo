package pl.kwasow.flamingo.backend.endpoints.albums

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.AlbumService
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.music.AlbumsGetResponse
import pl.kwasow.flamingo.types.user.UserDto

@RestController
class AlbumsController(
    private val albumService: AlbumService,
) {
    // ====== Endpoints
    @GetMapping("/albums/get")
    fun getAlbums(
        @AuthenticationPrincipal user: UserDto,
    ): AlbumsGetResponse {
        val albumDtoList = albumService.getAlbumsForCouple(user.couple.id)
        val albums = albumDtoList.map { Album(it) }

        return albums
    }
}
