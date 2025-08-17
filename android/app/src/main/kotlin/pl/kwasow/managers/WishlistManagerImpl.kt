package pl.kwasow.managers

import pl.kwasow.flamingo.types.wishlist.Wish

class WishlistManagerImpl(
    private val requestManager: RequestManager,
) : WishlistManager {
    // ====== Public methods
    override suspend fun getWishlist(): Map<Int, List<Wish>>? {
        val wishlist = requestManager.getWishlist() ?: return null
        val grouped = wishlist.sortedByDescending { it.timestamp }.groupBy { it.authorId }

        return grouped
    }

    override suspend fun addWish(
        authorId: Int,
        content: String,
    ): Boolean = requestManager.addWish(authorId, content, System.currentTimeMillis())

    override suspend fun removeWish(wish: Wish): Boolean = requestManager.removeWish(wish)

    override suspend fun updateWish(wish: Wish): Boolean = requestManager.updateWish(wish)
}
