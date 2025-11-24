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
import pl.kwasow.flamingo.types.memories.Memory
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class MemoriesEndpointAddTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `bob adding own memory succeeds`() {
        val newMemory =
            Memory(
                null,
                LocalDate.of(2025, 9, 1),
                null,
                "This is my new memory",
                "It is oh so sweet",
                null,
                TestData.ALICE_BOB_COUPLE_ID,
            )

        val request1 =
            requestBob(post("/memories/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

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

        assertEquals(3, memories.size)
        assert(2025 in memories.keys)
        val returnedMemory = memories[2025]?.get(0)
        assertEquals(newMemory.copy(id = returnedMemory?.id), returnedMemory)
    }

    @Transactional
    @Test
    fun `mallory adding memory to alice and bob fails`() {
        val newMemory =
            Memory(
                null,
                LocalDate.of(2023, 7, 31),
                LocalDate.of(2023, 8, 7),
                "First trip together",
                "We went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.ALICE_BOB_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }
}
