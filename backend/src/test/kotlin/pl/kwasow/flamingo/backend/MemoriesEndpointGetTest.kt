package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class MemoriesEndpointGetTest : BaseTest() {
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

        malloryMemories.forEach { (year, memoryList) ->
            if (year in aliceMemories.keys) {
                val malloryKeys = memoryList.map { it.id }.toSet()
                val aliceKeys = aliceMemories[year]?.map { it.id }?.toSet() ?: emptySet()

                assertEquals(emptySet<Int>(), malloryKeys.intersect(aliceKeys))
            }
        }
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

        assertEquals(setOf(2023, 2024), memories.keys)
        assertEquals(setOf(1, 2), memories[2023]?.map { it.id }?.toSet())
        assertEquals(setOf(3), memories[2024]?.map { it.id }?.toSet())
        assertEquals(true, memories[2023]?.all { it.coupleId == TestData.ALICE_BOB_COUPLE_ID })
        assertEquals(true, memories[2024]?.all { it.coupleId == TestData.ALICE_BOB_COUPLE_ID })
    }
}
