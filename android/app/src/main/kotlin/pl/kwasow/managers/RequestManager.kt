package pl.kwasow.managers

import android.location.Location
import pl.kwasow.data.types.Album
import pl.kwasow.data.types.AuthenticationResult
import pl.kwasow.data.types.UserLocation
import pl.kwasow.data.types.Wish
import pl.kwasow.flamingo.types.Memory

interface RequestManager {
    // ====== Methods
    suspend fun ping(): Boolean

    suspend fun getAuthenticatedUser(): AuthenticationResult

    suspend fun sendMissingYouMessage(): Boolean

    suspend fun getMemories(): Map<Int, List<Memory>>?

    suspend fun getWishlist(): List<Wish>?

    suspend fun addWish(
        authorId: Int,
        content: String,
        timestamp: Long,
    ): Boolean

    suspend fun updateWish(wish: Wish): Boolean

    suspend fun removeWish(wish: Wish): Boolean

    suspend fun getAlbums(): List<Album>?

    suspend fun getPartnerLocation(cached: Boolean = true): UserLocation?

    suspend fun updateLocation(location: Location): Boolean

    suspend fun updateFcmToken(token: String): Boolean
}
