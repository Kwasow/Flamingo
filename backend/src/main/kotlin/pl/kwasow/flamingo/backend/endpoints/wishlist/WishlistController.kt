package pl.kwasow.flamingo.backend.endpoints.wishlist

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.kwasow.flamingo.backend.services.WishlistService
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishDto
import pl.kwasow.flamingo.types.wishlist.WishlistAddResponse
import pl.kwasow.flamingo.types.wishlist.WishlistDeleteRequest
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import pl.kwasow.flamingo.types.wishlist.WishlistUpdateResponse

@RestController
class WishlistController(
    private val wishlistService: WishlistService,
) {
    // ====== Endpoints
    @GetMapping("/wishlist/get")
    fun getWishlist(
        @AuthenticationPrincipal user: User,
    ): WishlistGetResponse {
        val wishlistDto = wishlistService.getWishlistForCouple(user.couple)
        val wishlist = wishlistDto.map { Wish(it) }

        return wishlist
    }

    @PostMapping("/wishlist/add")
    fun addWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish,
    ): WishlistAddResponse {
        if (wishlistService.verifyWishForAdding(user, wish)) {
            val wishDto = wishlistService.saveWish(WishDto(wish))

            return Wish(wishDto)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/wishlist/update")
    fun updateWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish,
    ): WishlistUpdateResponse {
        if (wishlistService.verifyWishForEditing(user, wish)) {
            val wishDto = wishlistService.saveWish(WishDto(wish))

            return Wish(wishDto)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/wishlist/delete")
    fun deleteWish(
        @AuthenticationPrincipal user: User,
        @RequestBody deleteRequest: WishlistDeleteRequest,
    ) {
        if (wishlistService.verifyWishForDeletion(user, deleteRequest.id)) {
            wishlistService.deleteWish(deleteRequest.id)

            return
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}
