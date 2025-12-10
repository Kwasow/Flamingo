package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishlistAddResponse
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class WishlistAddTest : BaseTest() {
    @Test
    fun `bob can add own wish`() {
        val newWish =
            Wish(
                null,
                TestData.BOB_ID,
                "A belgian chocolate bar",
                false,
                1764058114,
            )

        val request1 =
            requestBob(post("/wishlist/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()
        val wish = json.decodeFromString<WishlistAddResponse>(result1.response.contentAsString)

        assertEquals(newWish.copy(id = wish.id), wish)

        val request2 = requestBob(get("/wishlist/get"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist = json.decodeFromString<WishlistGetResponse>(result2.response.contentAsString)

        assertEquals(3, wishlist.size)
        assert(wish in wishlist)
    }

    @Test
    fun `bob can add alice's wish`() {
        val newWish =
            Wish(
                null,
                TestData.ALICE_ID,
                "A belgian chocolate bar",
                false,
                1764058114,
            )

        val request1 =
            requestBob(post("/wishlist/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()
        val wish = json.decodeFromString<WishlistAddResponse>(result1.response.contentAsString)

        assertEquals(newWish.copy(id = wish.id), wish)

        val request2 = requestBob(get("/wishlist/get"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist = json.decodeFromString<WishlistGetResponse>(result2.response.contentAsString)

        assertEquals(3, wishlist.size)
        assert(wish in wishlist)
    }

    @Test
    fun `mallory can't add bob's wish`() {
        val newWish =
            Wish(
                null,
                TestData.BOB_ID,
                "A belgian chocolate bar",
                false,
                1764058114,
            )

        val request =
            requestMallory(post("/wishlist/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `mallory can't add alice's wish`() {
        val newWish =
            Wish(
                null,
                TestData.ALICE_ID,
                "A belgian chocolate bar",
                false,
                1764058114,
            )

        val request =
            requestMallory(post("/wishlist/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newWish))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }
}
