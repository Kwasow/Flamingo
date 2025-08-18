package pl.kwasow.managers

import pl.kwasow.flamingo.types.wishlist.Wish

interface WishlistManager {
    // ====== Methods
    suspend fun getWishlist(): Map<Int, List<Wish>>?

    suspend fun addWish(
        authorId: Int,
        content: String,
    ): Boolean

    suspend fun removeWish(wish: Wish): Boolean

    suspend fun updateWish(wish: Wish): Boolean
}
