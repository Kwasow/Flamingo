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
    ): Boolean {
        val newWish =
            Wish(
                null,
                authorId,
                content,
                false,
                System.currentTimeMillis(),
            )

        return requestManager.addWish(newWish)
    }

    override suspend fun removeWish(wishId: Int?): Boolean =
        wishId != null && requestManager.removeWish(wishId)

    override suspend fun updateWish(wish: Wish): Boolean = requestManager.updateWish(wish)
}
