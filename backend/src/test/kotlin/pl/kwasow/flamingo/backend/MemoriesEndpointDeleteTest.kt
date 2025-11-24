package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.memories.MemoryDeleteRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class MemoriesEndpointDeleteTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `bob deleting own memory succeeds`() {
        val request1 =
            requestBob(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoryDeleteRequest(3)))

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
            json
                .decodeFromString<Map<Int, List<Memory>>>(result.response.contentAsString)

        assertEquals(1, memories.size)
        assert(2023 in memories.keys)
    }

    @Transactional
    @Test
    fun `mallory deleting alice and bob's memory fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoryDeleteRequest(1)))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `empty body memory delete fails`() {
        val request = requestMallory(delete("/memories/delete"))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Transactional
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

    @Transactional
    @Test
    fun `deleting non existent memory fails`() {
        val request =
            requestMallory(delete("/memories/delete"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(MemoryDeleteRequest(42)))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }
}
