package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class MemoriesGetTest : BaseTest() {
    @Test
    fun `couple members memories match`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/memories/get")))
                .andExpect(status().isOk)
                .andReturn()
        val bobResult =
            mockMvc
                .perform(requestBob(get("/memories/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceMemories =
            json
                .decodeFromString<MemoriesGetResponse>(aliceResult.response.contentAsString)
        val bobMemories =
            json
                .decodeFromString<MemoriesGetResponse>(bobResult.response.contentAsString)

        assertEquals(bobMemories, aliceMemories)
    }

    @Test
    fun `intersection between different couples is empty`() {
        val aliceResult =
            mockMvc
                .perform(requestAlice(get("/memories/get")))
                .andExpect(status().isOk)
                .andReturn()
        val malloryResult =
            mockMvc
                .perform(requestMallory(get("/memories/get")))
                .andExpect(status().isOk)
                .andReturn()

        val aliceMemories =
            json
                .decodeFromString<MemoriesGetResponse>(aliceResult.response.contentAsString)
        val malloryMemories =
            json
                .decodeFromString<MemoriesGetResponse>(malloryResult.response.contentAsString)

        val aliceIds = aliceMemories.map { it.id }.toSet()
        val malloryIds = malloryMemories.map { it.id }.toSet()

        assert(aliceIds.intersect(malloryIds).isEmpty())
    }

    @Test
    fun `memories response matches expected format`() {
        val request = requestBob(get("/memories/get"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val memories =
            json
                .decodeFromString<MemoriesGetResponse>(result.response.contentAsString)

        assertEquals(3, memories.size)
    }
}
