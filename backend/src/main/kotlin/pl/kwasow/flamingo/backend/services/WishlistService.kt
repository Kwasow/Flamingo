package pl.kwasow.flamingo.backend.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.WishlistRepository
import pl.kwasow.flamingo.types.user.Couple
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishDto

@Service
class WishlistService(
    private val wishlistRepository: WishlistRepository,
) {
    // ====== Public methods
    fun getWishlistForCouple(couple: Couple): List<WishDto> =
        wishlistRepository.findByAuthorIdIn(couple.memberIds)

    fun saveWish(wish: WishDto): WishDto = wishlistRepository.save(wish)

    fun deleteWish(id: Int) = wishlistRepository.deleteById(id)

    fun findAuthor(wishId: Int): Int? = wishlistRepository.findByIdOrNull(wishId)?.authorId

    fun verifyWishForAdding(
        user: User,
        wish: Wish,
    ): Boolean = wish.authorId in user.couple.memberIds && wish.id == null

    fun verifyWishForEditing(
        user: User,
        wish: Wish,
    ): Boolean =
        wish.authorId in user.couple.memberIds &&
            wish.id != null &&
            findAuthor(wish.id!!) == wish.authorId

    fun verifyWishForDeletion(
        user: User,
        wishId: Int,
    ): Boolean = findAuthor(wishId) in user.couple.memberIds
}
