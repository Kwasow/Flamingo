package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `unauthenticated request to open endpoint should return 200`() {
        val request = get("/ping")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }

    @Transactional
    @Test
    fun `authenticated request to open endpoint should return 200`() {
        val request = getBob("/ping")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }

    @Transactional
    @Test
    fun `unauthenticated request to secured endpoint should return 403`() {
        val request = get("/auth")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Transactional
    @Test
    fun `authenticated request to secured endpoint should return 200`() {
        val request = getBob("/auth")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Transactional
    @Test
    fun `invalid token to secured endpoint should return 401`() {
        val request =
            get("/auth")
                .header("Authorization", "Bearer ${TestData.INVALID_TOKEN}")

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }
}
