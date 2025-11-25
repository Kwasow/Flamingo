package pl.kwasow.managers

import android.location.Location
import pl.kwasow.flamingo.types.auth.AuthenticationResult
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse

interface RequestManager {
    // ====== Methods
    suspend fun ping(): Boolean

    suspend fun getAuthenticatedUser(): AuthenticationResult

    suspend fun sendMissingYouMessage(): Boolean

    suspend fun getMemories(): MemoriesGetResponse?

    suspend fun getWishlist(): WishlistGetResponse?

    suspend fun addWish(wish: Wish): Boolean

    suspend fun updateWish(wish: Wish): Boolean

    suspend fun removeWish(id: Int): Boolean

    suspend fun getAlbums(): List<Album>?

    suspend fun getPartnerLocation(cached: Boolean = true): UserLocation?

    suspend fun updateLocation(location: Location): Boolean

    suspend fun updateFcmToken(token: String): Boolean
}
