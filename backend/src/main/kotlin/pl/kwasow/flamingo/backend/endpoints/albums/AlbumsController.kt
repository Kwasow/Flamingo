package pl.kwasow.flamingo.backend.endpoints.albums

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.AlbumService
import pl.kwasow.flamingo.types.music.AlbumsGetResponse
import pl.kwasow.flamingo.types.user.User

@RestController
class AlbumsController(
    private val albumService: AlbumService,
) {
    // ====== Endpoints
    @GetMapping("/albums/get")
    fun getAlbums(
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<AlbumsGetResponse> =
        ResponseEntity.ok(albumService.getAlbumsForCouple(user.couple.id))
}
