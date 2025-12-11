package pl.kwasow.flamingo.backend.configuration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class SecurityConfigurationTest : BaseTest() {
    @Test
    fun `unauthenticated request to open endpoint should return 200`() {
        val request = MockMvcRequestBuilders.get("/ping")

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `authenticated request to open endpoint should return 200`() {
        val request = requestBob(MockMvcRequestBuilders.get("/ping"))

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `unauthenticated request to secured endpoint should return 403`() {
        val request = MockMvcRequestBuilders.get("/auth")

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isForbidden)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `authenticated request to secured endpoint should return 200`() {
        val request = requestBob(MockMvcRequestBuilders.get("/auth"))

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `invalid token to secured endpoint should return 401`() {
        val request =
            MockMvcRequestBuilders
                .get("/auth")
                .header("Authorization", "Bearer ${TestData.INVALID_TOKEN}")

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }
}
