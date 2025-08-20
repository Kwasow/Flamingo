package pl.kwasow.flamingo.backend.endpoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.data.FirebaseToken
import pl.kwasow.flamingo.backend.repositories.CoupleRepository
import pl.kwasow.flamingo.backend.repositories.FirebaseTokenRepository
import pl.kwasow.flamingo.backend.repositories.MemoryRepository
import pl.kwasow.flamingo.backend.repositories.UserRepository
import pl.kwasow.flamingo.backend.repositories.WishlistRepository
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.Couple
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.flamingo.types.wishlist.Wish

@RestController
@RequestMapping("/test")
class TestController {
    // ====== Repositories
    @Autowired
    lateinit var coupleRepository: CoupleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var wishlistRepository: WishlistRepository

    @Autowired
    lateinit var memoryRepository: MemoryRepository

    @Autowired
    lateinit var tokenRepository: FirebaseTokenRepository

    // ====== Handlers
    @GetMapping
    fun error(): String = "Try /test/{tableName}"

    @GetMapping("/couples")
    fun getCouples(): List<Couple> = coupleRepository.findAll()

    @GetMapping("/users")
    fun getUsers(): List<User> = userRepository.findAll()

    @GetMapping("/wishlist")
    fun getWishlist(): List<Wish> = wishlistRepository.findAll()

    @GetMapping("/wishlist/{userId}")
    fun getUserWishlist(@PathVariable userId: Int): List<Wish> =
        wishlistRepository.findByAuthorId(userId)

    @GetMapping("/memories")
    fun getMemories(): List<Memory> = memoryRepository.findAll()

    @GetMapping("/tokens")
    fun getTokens(): List<FirebaseToken> = tokenRepository.findAll()
}
