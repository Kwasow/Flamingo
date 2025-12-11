package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.AlbumRepository
import pl.kwasow.flamingo.types.music.AlbumDto

@Service
class AlbumService(
    private val albumRepository: AlbumRepository,
) {
    // ====== Public methods
    fun getAlbumsForCouple(coupleId: Int): List<AlbumDto> = albumRepository.findByCoupleId(coupleId)
}
