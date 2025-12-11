package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.memories.MemoriesDeleteRequest
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class MemoriesDeleteTest : BaseTest() {
    @Test
    fun `bob deleting own memory succeeds`() {
        val request1 =
            requestBob(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoriesDeleteRequest(3)))

        mockMvc
            .perform(request1)
            .andExpect(status().isOk)

        val request2 = requestBob(get("/memories/get"))

        val result =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val memories =
            json.decodeFromString<MemoriesGetResponse>(result.response.contentAsString)

        assertEquals(1, memories.size)
        assert(2023 in memories.keys)
    }

    @Test
    fun `mallory deleting alice and bob's memory fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoriesDeleteRequest(1)))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `empty body memory delete fails`() {
        val request = requestMallory(delete("/memories/delete"))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `null id memory delete fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": null}")

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `deleting non existent memory fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoriesDeleteRequest(42)))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    fun `deleting null id memory fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": null}")

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }
}
