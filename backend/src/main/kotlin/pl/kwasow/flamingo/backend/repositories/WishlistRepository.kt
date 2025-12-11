package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.wishlist.WishDto

interface WishlistRepository : JpaRepository<WishDto, Int> {
    // ====== Public methods
    fun findByAuthorIdIn(userIds: List<Int>): List<WishDto>
}
