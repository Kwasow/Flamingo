package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.wishlist.Wish

interface WishlistRepository : JpaRepository<Wish, Int> {
    // ====== Public methods
    fun findByAuthorIdIn(userIds: List<Int>): List<Wish>
}
