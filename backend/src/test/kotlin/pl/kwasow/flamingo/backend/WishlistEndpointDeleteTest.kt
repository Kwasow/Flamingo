package pl.kwasow.flamingo.backend

import kotlinx.serialization.encodeToString
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.wishlist.WishlistDeleteRequest
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class WishlistEndpointDeleteTest : BaseTest() {
    @Test
    fun `bob deleting own wish succeeds`() {
        val request1 =
            requestBob(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(WishlistDeleteRequest(2)))

        mockMvc
            .perform(request1)
            .andExpect(status().isOk)

        val request2 = requestBob(get("/wishlist/get"))

        val result =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist =
            json.decodeFromString<WishlistGetResponse>(result.response.contentAsString)

        assertEquals(1, wishlist.size)
        assertEquals(setOf(1), wishlist.map { it.id }.toSet())
    }

    @Test
    fun `bob deleting alice's wish succeeds`() {
        val request1 =
            requestBob(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(WishlistDeleteRequest(1)))

        mockMvc
            .perform(request1)
            .andExpect(status().isOk)

        val request2 = requestBob(get("/wishlist/get"))

        val result =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val wishlist =
            json.decodeFromString<WishlistGetResponse>(result.response.contentAsString)

        assertEquals(1, wishlist.size)
        assertEquals(setOf(2), wishlist.map { it.id }.toSet())
    }

    @Test
    fun `mallory deleting alice's wish fails`() {
        val request1 =
            requestMallory(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(WishlistDeleteRequest(1)))

        mockMvc
            .perform(request1)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `mallory deleting bob's wish fails`() {
        val request1 =
            requestMallory(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(WishlistDeleteRequest(2)))

        mockMvc
            .perform(request1)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `empty body wish delete fails`() {
        val request = requestMallory(delete("/wishlist/delete"))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `null id wish delete fails`() {
        val request =
            requestMallory(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": null}")

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `deleting non existent wish fails`() {
        val request =
            requestMallory(delete("/wishlist/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(WishlistDeleteRequest(42)))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }
}
