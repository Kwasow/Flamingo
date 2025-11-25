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
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import pl.kwasow.flamingo.types.memories.MemoriesUpdateResponse
import pl.kwasow.flamingo.types.memories.Memory
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class MemoriesEndpointUpdateTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `updating all memory fields succeeds`() {
        val updatedMemory =
            Memory(
                1,
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 8, 8),
                "First trip together (edited)",
                "We went to a shopping mall and got lost (edited)",
                "https://examplephotos.org/mall_edited",
                TestData.ALICE_BOB_COUPLE_ID,
            )

        val request1 =
            requestBob(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(updatedMemory))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()
        val memory = json.decodeFromString<MemoriesUpdateResponse>(result1.response.contentAsString)

        assertEquals(updatedMemory, memory)

        val request2 = requestBob(get("/memories/get"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()
        val memories =
            json
                .decodeFromString<MemoriesGetResponse>(result2.response.contentAsString)

        assertEquals(2, memories.size)
        assert(2023 in memories.keys)
        assertEquals(
            updatedMemory,
            memories[2023]?.find { it.id == updatedMemory.id },
        )
    }

    @Transactional
    @Test
    fun `mallory updating alice and bob's memory fails`() {
        val newMemory =
            Memory(
                1,
                LocalDate.of(2025, 7, 31),
                LocalDate.of(2025, 8, 7),
                "First trip together (Mallory was here)",
                "We went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.ALICE_BOB_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `mallory making own memory alice and bob's fails`() {
        val newMemory =
            Memory(
                4,
                LocalDate.of(2024, 7, 31),
                null,
                "I spied on Alice and Bob",
                "They went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.ALICE_BOB_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `mallory stealing alice and bob's memory fails`() {
        val newMemory =
            Memory(
                1,
                LocalDate.of(2023, 7, 31),
                LocalDate.of(2023, 8, 7),
                "First trip together",
                "We went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.MALLORY_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `updating non existent memory fails`() {
        val newMemory =
            Memory(
                42,
                LocalDate.of(2023, 7, 31),
                null,
                "First trip together",
                "We went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.MALLORY_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Transactional
    @Test
    fun `updating null id memory fails`() {
        val newMemory =
            Memory(
                null,
                LocalDate.of(2023, 7, 31),
                null,
                "First trip together",
                "We went to a shopping mall and got lost",
                "https://examplephotos.org/mall",
                TestData.MALLORY_COUPLE_ID,
            )

        val request =
            requestMallory(post("/memories/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newMemory))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Transactional
    @Test
    fun `empty body memory update fails`() {
        val request = requestMallory(post("/memories/update"))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }
}
