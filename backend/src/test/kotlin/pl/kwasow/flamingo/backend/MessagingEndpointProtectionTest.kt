package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class MessagingEndpointProtectionTest : BaseTest() {
    @Test
    fun `updateFcmToken is protected`() {
        testProtection(mockMvc, post("/messaging/updateFcmToken"))
    }

    @Test
    fun `send endpoint is protected`() {
        testProtection(mockMvc, post("/messaging/send"))
    }
}
