package pl.kwasow.flamingo.backend.endpoints.wishlist

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.WishlistService
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.wishlist.Wish
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
    ): ResponseEntity<WishlistGetResponse> =
        ResponseEntity.ok().body(wishlistService.getWishlistForCouple(user.couple))

    @PostMapping("/wishlist/add")
    fun addWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish,
    ): ResponseEntity<WishlistAddResponse> {
        if (wishlistService.verifyWishForAdding(user, wish)) {
            return ResponseEntity
                .ok()
                .body(wishlistService.saveWish(wish))
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @PostMapping("/wishlist/update")
    fun updateWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish,
    ): ResponseEntity<WishlistUpdateResponse> {
        if (wishlistService.verifyWishForEditing(user, wish)) {
            return ResponseEntity
                .ok()
                .body(wishlistService.saveWish(wish))
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @DeleteMapping("/wishlist/delete")
    fun deleteWish(
        @AuthenticationPrincipal user: User,
        @RequestBody deleteRequest: WishlistDeleteRequest,
    ): ResponseEntity<Any> {
        if (wishlistService.verifyWishForDeletion(user, deleteRequest.id)) {
            wishlistService.deleteWish(deleteRequest.id)

            return ResponseEntity
                .ok()
                .build()
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }
}
