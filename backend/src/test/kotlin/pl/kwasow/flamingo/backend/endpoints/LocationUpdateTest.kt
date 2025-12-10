package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.location.UserLocation
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class LocationUpdateTest : BaseTest() {
    @Test
    fun `bob can update self location`() {
        val newLocation =
            UserLocation(
                TestData.BOB_ID,
                -2.2,
                -2.2,
                12.5f,
                123,
            )

        val request1 =
            requestBob(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()

        val updatedLocation = json.decodeFromString<UserLocation>(result1.response.contentAsString)
        assertEquals(newLocation, updatedLocation)

        val request2 = requestBob(get("/location/get/self"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()

        val getLocation = json.decodeFromString<UserLocation>(result2.response.contentAsString)
        assertEquals(newLocation, getLocation)
    }

    @Test
    fun `bob can't update alice's location`() {
        val newLocation =
            UserLocation(
                TestData.ALICE_ID,
                -2.2,
                -2.2,
                12.5f,
                123,
            )

        val request =
            requestBob(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `mallory can update own location`() {
        val newLocation =
            UserLocation(
                TestData.MALLORY_ID,
                -3.3,
                -3.3,
                13.5f,
                124,
            )

        val request1 =
            requestMallory(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        val result1 =
            mockMvc
                .perform(request1)
                .andExpect(status().isOk)
                .andReturn()

        val updatedLocation = json.decodeFromString<UserLocation>(result1.response.contentAsString)
        assertEquals(newLocation, updatedLocation)

        val request2 = requestMallory(get("/location/get/self"))

        val result2 =
            mockMvc
                .perform(request2)
                .andExpect(status().isOk)
                .andReturn()

        val getLocation = json.decodeFromString<UserLocation>(result2.response.contentAsString)
        assertEquals(newLocation, getLocation)
    }

    @Test
    fun `mallory can't update alice's location`() {
        val newLocation =
            UserLocation(
                TestData.ALICE_ID,
                -2.2,
                -2.2,
                12.5f,
                123,
            )

        val request =
            requestMallory(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `mallory tries incorrect longitude and latitude`() {
        val matrix =
            listOf(
                Pair(-190.0, 0.0),
                Pair(190.0, 0.0),
                Pair(0.0, -190.0),
                Pair(0.0, 190.0),
                Pair(-190.0, -190.0),
                Pair(190.0, -190.0),
                Pair(-190.0, 190.0),
                Pair(190.0, 190.0),
            )

        for ((latitude, longitude) in matrix) {
            val newLocation =
                UserLocation(
                    userId = TestData.MALLORY_ID,
                    latitude = latitude,
                    longitude = longitude,
                    accuracy = 1f,
                    timestamp = System.currentTimeMillis(),
                )

            val request =
                requestMallory(post("/location/update"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.encodeToString(newLocation))

            mockMvc
                .perform(request)
                .andExpect(status().isUnauthorized)
        }
    }
}
