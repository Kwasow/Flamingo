package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class PingTest : BaseTest() {
    @Test
    fun `ping responds with pong`() {
        val request = get("/ping")

        val result =
            mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        assertEquals("PONG", result.response.contentAsString)
    }
}
