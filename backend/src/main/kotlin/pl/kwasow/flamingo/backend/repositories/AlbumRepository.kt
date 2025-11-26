package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.music.Album

interface AlbumRepository : JpaRepository<Album, Int> {
    fun findByCoupleId(coupleId: Int): List<Album>
}
