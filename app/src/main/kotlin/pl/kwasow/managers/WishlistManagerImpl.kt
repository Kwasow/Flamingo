package pl.kwasow.managers

import pl.kwasow.flamingo.types.wishlist.Wish
import java.time.LocalDate

class WishlistManagerImpl(
    private val requestManager: RequestManager,
) : WishlistManager {
    // ====== Public methods
    override suspend fun getWishlist(): Map<Int, List<Wish>>? {
        val wishlist = requestManager.getWishlist() ?: return null
        val grouped = wishlist.sortedByDescending { it.date }.groupBy { it.authorId }

        return grouped
    }

    override suspend fun addWish(
        authorId: Int,
        content: String,
    ): Boolean {
        val newWish =
            Wish(
                id = null,
                authorId = authorId,
                content = content,
                done = false,
                date = LocalDate.now(),
            )

        return requestManager.addWish(newWish)
    }

    override suspend fun removeWish(wishId: Int?): Boolean =
        wishId != null && requestManager.removeWish(wishId)

    override suspend fun updateWish(wish: Wish): Boolean = requestManager.updateWish(wish)
}
