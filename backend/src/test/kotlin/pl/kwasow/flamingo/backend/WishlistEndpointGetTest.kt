package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.map
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class WishlistEndpointGetTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `couple members wishlist matches`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/wishlist/get")))
                .andExpect(status().isOk)
                .andReturn()
        val bobResult =
            mockMvc
                .perform(requestBob(get("/wishlist/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceWishlist =
            json
                .decodeFromString<WishlistGetResponse>(aliceResult.response.contentAsString)
        val bobWishlist =
            json
                .decodeFromString<WishlistGetResponse>(bobResult.response.contentAsString)

        assertEquals(bobWishlist, aliceWishlist)
    }

    @Transactional
    @Test
    fun `intersection between different couples is empty`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/wishlist/get")))
                .andExpect(status().isOk)
                .andReturn()
        val malloryResult =
            mockMvc
                .perform(requestMallory(get("/wishlist/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceWishlist =
            json
                .decodeFromString<WishlistGetResponse>(aliceResult.response.contentAsString)
        val malloryWishlist =
            json
                .decodeFromString<WishlistGetResponse>(malloryResult.response.contentAsString)

        malloryWishlist.forEach { wish ->
            assert(wish !in aliceWishlist)
        }
    }

    @Transactional
    @Test
    fun `wishlist response matches expected format`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/wishlist/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceWishlist =
            json
                .decodeFromString<WishlistGetResponse>(aliceResult.response.contentAsString)

        assertEquals(2, aliceWishlist.size)
        assertEquals(setOf(1, 2), aliceWishlist.map { it.id }.toSet())
    }
}
