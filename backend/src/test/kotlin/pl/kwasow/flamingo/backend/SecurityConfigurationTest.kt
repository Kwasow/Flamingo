package pl.kwasow.flamingo.backend

import com.google.firebase.auth.FirebaseAuth
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var firebaseAuth: FirebaseAuth

    @Test
    fun `unauthenticated request to open endpoint should return 200`() {
        mockMvc
            .perform(get("/api/ping"))
            .andExpect(status().isOk)
    }

//    @Test
//    fun `authenticated request to open endpoint should return 200`() {
//
//    }

    @Test
    fun `unauthenticated request to secured endpoint should return 401`() {
        mockMvc
            .perform(get("/api/auth"))
            .andExpect(status().isUnauthorized)
    }

//    @Test
//    fun `authenticated request to secured endpoint should return 200`() {
//
//    }

    @Test
    fun `invalid token to secured endpoint should return 401`() {
        whenever(firebaseAuth.verifyIdToken(any()))
            .thenThrow(RuntimeException("Invalid token"))

        mockMvc.perform(get("/api/auth")
                .header("Authorization", "Bearer invalid-token")
        ).andExpect(status().isUnauthorized)
    }
}
