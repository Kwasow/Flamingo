package pl.kwasow.flamingo.backend

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest : BaseTest() {
    companion object {
        const val VALID_TOKEN = "valid-token"
        const val VALID_EMAIL = "bob@example.com"
        const val INVALID_TOKEN = "invalid-token"
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseToken: FirebaseToken

    @BeforeEach
    fun setup() {
        SecurityContextHolder.clearContext()

        // Setup correct test tokens
        whenever(firebaseAuth.verifyIdToken(VALID_TOKEN))
            .thenReturn(firebaseToken)
        whenever(firebaseToken.email)
            .thenReturn(VALID_EMAIL)

        // Setup incorrect test tokens
        val exception =
            FirebaseAuthException(
                FirebaseException(
                    ErrorCode.UNKNOWN,
                    "Invalid token",
                    Exception("Test INVALID_TOKEN"),
                ),
            )
        whenever(firebaseAuth.verifyIdToken(INVALID_TOKEN))
            .thenThrow(exception)
    }

    @Test
    fun `unauthenticated request to open endpoint should return 200`() {
        val request = get("/ping")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }

    @Test
    fun `authenticated request to open endpoint should return 200`() {
        val request =
            get("/ping")
                .header("Authorization", "Bearer $VALID_TOKEN")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }

    @Test
    fun `unauthenticated request to secured endpoint should return 403`() {
        val request = get("/auth")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `authenticated request to secured endpoint should return 200`() {
        val request =
            get("/auth")
                .header("Authorization", "Bearer $VALID_TOKEN")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `invalid token to secured endpoint should return 401`() {
        val request =
            get("/auth")
                .header("Authorization", "Bearer $INVALID_TOKEN")

        mockMvc
            .perform(request)
            .andExpect(status().isUnauthorized)

        assertEquals(null, SecurityContextHolder.getContext().authentication)
    }
}
