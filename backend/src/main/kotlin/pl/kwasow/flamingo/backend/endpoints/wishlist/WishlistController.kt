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

@RestController
class WishlistController(
    private val wishlistService: WishlistService
) {
    // ====== Endpoints
    @GetMapping("/wishlist/get")
    fun getWishlist(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<Wish>> {
        return ResponseEntity.ok().body(wishlistService.getWishlistForCouple(user.couple))
    }

    @PostMapping("/wishlist/add")
    fun addWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish
    ): ResponseEntity<Wish> {
        val incomingWish = wish.copy(id = null)
        if (!verifyAuthor(user, incomingWish)) {
            return ResponseEntity
                .badRequest()
                .body(null)
        }

        val newWish = wishlistService.saveWish(incomingWish)

        return ResponseEntity
            .ok()
            .body(newWish)
    }

    @PostMapping("/wishlist/update")
    fun updateWish(
        @AuthenticationPrincipal user: User,
        @RequestBody wish: Wish
    ): ResponseEntity<Wish?> {
        if (!verifyAuthor(user, wish)) {
            return ResponseEntity
                .badRequest()
                .body(null)
        }

        val errorResponse = verifyWish(user, wish.id)

        if (errorResponse != null) {
            return errorResponse
        }

        val editedMemory = wishlistService.saveWish(wish)

        return ResponseEntity
            .ok()
            .body(editedMemory)
    }

    @DeleteMapping
    fun deleteWish(
        @AuthenticationPrincipal user: User,
        id: Int,
    ): ResponseEntity<*> {
        val errorResponse = verifyWish(user, id)

        if (errorResponse != null) {
            return errorResponse
        }

        wishlistService.deleteWish(id)

        return ResponseEntity
            .ok()
            .build<Any>()
    }

    // ====== Private methods
    private fun verifyAuthor(user: User, authorId: Int): Boolean =
        user.couple.getMemberIds().contains(authorId)

    private fun verifyAuthor(user: User, wish: Wish): Boolean =
        verifyAuthor(user, wish.authorId)

    private fun verifyWish(user: User, id: Int?): ResponseEntity<Wish?>? {
        // Check if ID is set
        val incomingId = id ?: return ResponseEntity
            .badRequest()
            .build()

        // Check if the user is authorized to modify this wish
        val savedAuthorId = wishlistService.findAuthor(incomingId)
            ?: return ResponseEntity
                .badRequest()
                .build()

        if (!verifyAuthor(user, savedAuthorId)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }

        return null
    }
}
