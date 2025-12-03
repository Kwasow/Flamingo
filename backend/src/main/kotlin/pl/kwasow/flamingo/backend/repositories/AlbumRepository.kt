package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.music.AlbumDto

interface AlbumRepository : JpaRepository<AlbumDto, Int> {
    fun findByCoupleId(coupleId: Int): List<AlbumDto>
}
