package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.memories.Memory
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class MemoriesEndpointGetTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
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
                .decodeFromString<Map<Int, List<Memory>>>(aliceResult.response.contentAsString)
        val bobMemories =
            json
                .decodeFromString<Map<Int, List<Memory>>>(bobResult.response.contentAsString)

        assertEquals(bobMemories, aliceMemories)
    }

    @Transactional
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
                .decodeFromString<Map<Int, List<Memory>>>(aliceResult.response.contentAsString)
        val malloryMemories =
            json
                .decodeFromString<Map<Int, List<Memory>>>(malloryResult.response.contentAsString)

        malloryMemories.forEach { (year, memoryList) ->
            if (year in aliceMemories.keys) {
                val malloryKeys = memoryList.map { it.id }.toSet()
                val aliceKeys = aliceMemories[year]?.map { it.id }?.toSet() ?: emptySet()

                assertEquals(emptySet<Int>(), malloryKeys.intersect(aliceKeys))
            }
        }
    }

    @Transactional
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
                .decodeFromString<Map<Int, List<Memory>>>(result.response.contentAsString)

        assertEquals(2, memories.size)
        assert(2023 in memories.keys)
        assert(2024 in memories.keys)
        assertEquals(2, memories[2023]?.size)
        assert(1 in memories[2023]?.map { it.id }!!)
        assert(2 in memories[2023]?.map { it.id }!!)
        assert(memories[2023]!!.all { it.coupleId == TestData.ALICE_BOB_COUPLE_ID })
        assertEquals(1, memories[2024]?.size)
        assert(3 in memories[2024]?.map { it.id }!!)
        assert(memories[2024]!!.all { it.coupleId == TestData.ALICE_BOB_COUPLE_ID })
    }
}
