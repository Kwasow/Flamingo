package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import pl.kwasow.flamingo.types.wishlist.WishlistUpdateResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class WishlistEndpointUpdateTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `bob updating own wish succeeds`() {
        val newWish =
            Wish(
                2,
                TestData.BOB_ID,
                "A newer belt for my suit",
                false,
                1713720737,
            )

        val request1 =
            requestBob(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()
        val wish = json.decodeFromString<WishlistUpdateResponse>(result1.response.contentAsString)

        assertEquals(newWish, wish)

        val request2 = requestBob(get("/wishlist/get"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist = json.decodeFromString<WishlistGetResponse>(result2.response.contentAsString)

        assertEquals(setOf(1, 2), wishlist.map { it.id }.toSet())
        assert(wish in wishlist)
    }

    @Transactional
    @Test
    fun `bob updating alice's wish succeeds`() {
        val newWish =
            Wish(
                1,
                TestData.ALICE_ID,
                "A black kitten",
                false,
                1722548749,
            )

        val request1 =
            requestBob(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()
        val wish = json.decodeFromString<WishlistUpdateResponse>(result1.response.contentAsString)

        assertEquals(newWish, wish)

        val request2 = requestBob(get("/wishlist/get"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist = json.decodeFromString<WishlistGetResponse>(result2.response.contentAsString)

        assertEquals(setOf(1, 2), wishlist.map { it.id }.toSet())
        assert(wish in wishlist)
    }

    @Transactional
    @Test
    fun `bob stealing alice's wish fails`() {
        val newWish =
            Wish(
                1,
                TestData.BOB_ID,
                "A kitten",
                false,
                1722548749,
            )

        val request1 =
            requestBob(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request1)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `mallory stealing alice's wish fails`() {
        val newWish =
            Wish(
                1,
                TestData.MALLORY_ID,
                "A kitten",
                false,
                1722548749,
            )

        val request1 =
            requestMallory(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request1)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `mallory updating alice's wish fails`() {
        val newWish =
            Wish(
                1,
                TestData.ALICE_ID,
                "A kitten (mallory was here)",
                true,
                1722548749,
            )

        val request1 =
            requestMallory(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request1)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `updating non existent wish fails`() {
        val newWish =
            Wish(
                42,
                TestData.BOB_ID,
                "A newer belt for my suit",
                false,
                1713720737,
            )

        val request =
            requestMallory(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `updating null id wish fails`() {
        val newWish =
            Wish(
                null,
                TestData.BOB_ID,
                "A newer belt for my suit",
                false,
                1713720737,
            )

        val request =
            requestBob(post("/wishlist/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Transactional
    @Test
    fun `empty body wish update fails`() {
        val request = requestMallory(post("/wishlist/update"))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }
}
