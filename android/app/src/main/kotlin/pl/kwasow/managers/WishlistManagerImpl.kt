package pl.kwasow.managers

import pl.kwasow.data.types.Wish

class WishlistManagerImpl(
    private val requestManager: RequestManager,
) : WishlistManager {
    // ====== Public methods
    override suspend fun getWishlist(): Map<String, List<Wish>>? {
        val wishlist = requestManager.getWishlist() ?: return null
        val grouped = wishlist.sortedByDescending { it.timestamp }.groupBy { it.author }

        return grouped
    }

    override suspend fun addWish(
        author: String,
        content: String,
    ): Boolean = requestManager.addWish(author, content, System.currentTimeMillis())

    override suspend fun removeWish(wish: Wish): Boolean = requestManager.removeWish(wish)

    override suspend fun updateWish(wish: Wish): Boolean = requestManager.updateWish(wish)
}
