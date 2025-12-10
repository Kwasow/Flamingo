package pl.kwasow.flamingo.backend.endpoints

import org.opentest4j.TestAbortedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.repositories.FirebaseTokenRepository
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.messaging.FcmUpdateTokenRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class MessagingUpdateTokenTest : BaseTest() {
    @Autowired
    lateinit var firebaseTokenRepository: FirebaseTokenRepository

    @Test
    fun `adding new token succeeds`() {
        val newToken =
            FcmUpdateTokenRequest(TestData.BOB_FCM_TOKEN + TestData.ALICE_FCM_TOKEN, false)

        val request =
            requestBob(post("/messaging/updateFcmToken"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(newToken))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        val bobTokens = firebaseTokenRepository.findByUserId(TestData.BOB_ID)

        assertEquals(2, bobTokens.size)
        assertEquals(1, bobTokens.count { it.token == newToken.token })
    }

    @Test
    fun `refreshing existing token succeeds`() {
        val oldTimestamp =
            firebaseTokenRepository.findByToken(TestData.BOB_FCM_TOKEN)?.lastSeen
                ?: throw TestAbortedException("Invalid initial configuration")

        val requestToken = FcmUpdateTokenRequest(TestData.BOB_FCM_TOKEN, false)

        val request =
            requestBob(post("/messaging/updateFcmToken"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.encodeToString(requestToken))

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        val bobTokens = firebaseTokenRepository.findByUserId(TestData.BOB_ID)

        assertEquals(1, bobTokens.size)
        assertEquals(TestData.BOB_FCM_TOKEN, bobTokens[0].token)
        assert(bobTokens[0].lastSeen > oldTimestamp)
    }
}
