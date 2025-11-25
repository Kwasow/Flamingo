package pl.kwasow.flamingo.types.wishlist

import kotlinx.serialization.Serializable

@Serializable
data class WishlistDeleteRequest(
    val id: Int,
)
