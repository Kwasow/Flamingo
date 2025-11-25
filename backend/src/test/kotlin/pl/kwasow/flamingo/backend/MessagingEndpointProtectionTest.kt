package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class MessagingEndpointProtectionTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `updateFcmToken is protected`() {
        testProtection(mockMvc, post("/messaging/updateFcmToken"))
    }

    @Transactional
    @Test
    fun `send endpoint is protected`() {
        testProtection(mockMvc, post("/messaging/send"))
    }
}
