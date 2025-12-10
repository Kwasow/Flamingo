package pl.kwasow.flamingo.backend.endpoints

import com.google.firebase.messaging.MulticastMessage
import org.mockito.Captor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.messaging.FcmSendMessageRequest
import pl.kwasow.flamingo.types.messaging.MessageType
import kotlin.collections.get
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class MessagingSendTest : BaseTest() {
    @Captor
    val argumentCaptor = argumentCaptor<MulticastMessage>()

    @Test
    fun `bob can send a missing you message to alice`() {
        val notification = FcmSendMessageRequest.MISSING_YOU

        val request =
            requestBob(post("/messaging/send"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(notification))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        verify(firebaseMessaging, times(1))
            .sendEachForMulticast(argumentCaptor.capture())

        val message = argumentCaptor.lastValue
        val data = ReflectionTestUtils.getField(message, "data") as Map<*, *>
        val tokens = ReflectionTestUtils.getField(message, "tokens") as List<*>

        assertEquals(listOf(TestData.ALICE_FCM_TOKEN), tokens)
        assertEquals(MessageType.MISSING_YOU.id, data["type"])
        assertEquals(TestData.BOB_NAME, data["name"])
    }

    @Test
    fun `mallory can't send a missing you message`() {
        val notification = FcmSendMessageRequest.MISSING_YOU

        val request =
            requestMallory(post("/messaging/send"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(notification))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `user not allowed to send daily notification`() {
        val notification = FcmSendMessageRequest.DAILY_MEMORY

        val request =
            requestMallory(post("/messaging/send"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(notification))

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest)
    }
}
