package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class MessagingProtectionTest : BaseTest() {
    @Test
    fun `updateFcmToken is protected`() {
        testProtection(post("/messaging/updateFcmToken"))
    }

    @Test
    fun `send endpoint is protected`() {
        testProtection(post("/messaging/send"))
    }
}
