package pl.kwasow.flamingo.backend.endpoints

import com.google.firebase.messaging.MulticastMessage
import kotlinx.serialization.json.Json
import org.mockito.Captor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.messaging.MessageType
import kotlin.collections.get
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class LocationMessagingTest : BaseTest() {
    @Captor
    val argumentCaptor = argumentCaptor<MulticastMessage>()

    @Test
    fun `location update test (self update)`() {
        // STEP 1 - Bob updates location
        val newLocation =
            UserLocation(
                TestData.BOB_ID,
                -3.3,
                -3.3,
                13.5f,
                124,
            )

        val request =
            requestBob(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        // STEP 2 - Alice receives update
        verify(firebaseMessaging, times(1))
            .sendEachForMulticast(argumentCaptor.capture())

        val message = argumentCaptor.lastValue
        val data = ReflectionTestUtils.getField(message, "data") as Map<*, *>
        val tokens = ReflectionTestUtils.getField(message, "tokens") as List<*>

        assertEquals(listOf(TestData.ALICE_FCM_TOKEN), tokens)
        assertEquals(MessageType.LOCATION_UPDATED.id, data["type"])

        val userLocationJson = data["user_location_json"] as String
        val userLocation = Json.decodeFromString<UserLocation>(userLocationJson)

        assertEquals(newLocation, userLocation)
    }

    @Test
    fun `full communication test (partner get)`() {
        // STEP 1 - Bob gets Alice's location
        val request1 = requestBob(get("/location/get/partner"))

        mockMvc
            .perform(request1)
            .andExpect(status().isOk)

        // STEP 2 - Alice receives request
        verify(firebaseMessaging, times(1))
            .sendEachForMulticast(argumentCaptor.capture())

        val message1 = argumentCaptor.lastValue
        val data1 = ReflectionTestUtils.getField(message1, "data") as Map<*, *>
        val tokens1 = ReflectionTestUtils.getField(message1, "tokens") as List<*>

        assertEquals(listOf(TestData.ALICE_FCM_TOKEN), tokens1)
        assertEquals(MessageType.REQUEST_LOCATION.id, data1["type"])

        // STEP 3 - Alice updates location
        val newLocation =
            UserLocation(
                TestData.ALICE_ID,
                -3.3,
                -3.3,
                13.5f,
                124,
            )

        val request2 =
            requestAlice(post("/location/update"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newLocation))

        mockMvc
            .perform(request2)
            .andExpect(status().isOk)

        // STEP 4 - Bob receives location update
        verify(firebaseMessaging, times(2))
            .sendEachForMulticast(argumentCaptor.capture())

        val message2 = argumentCaptor.lastValue
        val data2 = ReflectionTestUtils.getField(message2, "data") as Map<*, *>
        val tokens2 = ReflectionTestUtils.getField(message2, "tokens") as List<*>

        assertEquals(listOf(TestData.BOB_FCM_TOKEN), tokens2)
        assertEquals(MessageType.LOCATION_UPDATED.id, data2["type"])

        val userLocationJson = data2["user_location_json"] as String
        val userLocation = Json.decodeFromString<UserLocation>(userLocationJson)

        assertEquals(newLocation, userLocation)
    }
}
