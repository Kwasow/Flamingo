package pl.kwasow.flamingo.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.repositories.UserLocationRepository
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.location.LocationGetResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class LocationEndpointGetTest : BaseTest() {
    @Autowired
    lateinit var userLocationRepository: UserLocationRepository

    @Test
    fun `bob can get own location`() {
        val request = requestBob(get("/location/get/self"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val bobLocation =
            json
                .decodeFromString<LocationGetResponse>(result.response.contentAsString)

        assertEquals(TestData.BOB_ID, bobLocation?.userId)
        assertEquals(2.2, bobLocation?.latitude)
        assertEquals(2.2, bobLocation?.longitude)
    }

    @Test
    fun `bob can get alice's location`() {
        val request = requestBob(get("/location/get/partner"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val aliceLocation =
            json
                .decodeFromString<LocationGetResponse>(result.response.contentAsString)

        assertEquals(TestData.ALICE_ID, aliceLocation?.userId)
        assertEquals(1.1, aliceLocation?.latitude)
        assertEquals(1.1, aliceLocation?.longitude)
    }

    @Test
    fun `mallory can't get partner location`() {
        val request = requestMallory(get("/location/get/partner"))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `mallory gets null own location when not set`() {
        // Setup test
        userLocationRepository.deleteById(TestData.MALLORY_ID)

        // Run test
        val request = requestMallory(get("/location/get/self"))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
            .andExpect { it.response.contentAsString == "null" }
    }

    @Test
    fun `bob gets null partner location when not set`() {
        // Setup test
        userLocationRepository.deleteById(TestData.ALICE_ID)

        // Run test
        val request = requestBob(get("/location/get/partner"))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
            .andExpect { it.response.contentAsString == "null" }
    }
}
