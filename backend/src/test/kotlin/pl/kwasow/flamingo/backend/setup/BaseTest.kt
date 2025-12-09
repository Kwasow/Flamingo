package pl.kwasow.flamingo.backend.setup

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.messaging.FirebaseMessaging
import jakarta.transaction.Transactional
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.mariadb.MariaDBContainer
import org.testcontainers.utility.DockerImageName
import pl.kwasow.flamingo.types.user.UserIcon
import java.time.LocalDate

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
abstract class BaseTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Mock
    val aliceFirebaseToken =
        mock<FirebaseToken> {
            on { email } doReturn TestData.ALICE_EMAIL
        }

    @Mock
    val bobFirebaseToken =
        mock<FirebaseToken> {
            on { email } doReturn TestData.BOB_EMAIL
        }

    @Mock
    val malloryFirebaseToken =
        mock<FirebaseToken> {
            on { email } doReturn TestData.MALLORY_EMAIL
        }

    @MockitoBean
    private lateinit var firebaseAuth: FirebaseAuth

    @MockitoBean
    protected lateinit var firebaseMessaging: FirebaseMessaging

    @MockitoBean
    private lateinit var firebaseApp: FirebaseApp

    protected val json = Json

    protected class TestData {
        companion object {
            val ALICE_BOB_ANNIVERSARY: LocalDate = LocalDate.parse("2023-07-31")
            const val ALICE_BOB_COUPLE_ID = 1

            const val ALICE_ID = 1
            const val ALICE_TOKEN = "alice-token"
            const val ALICE_EMAIL = "alice@example.com"
            const val ALICE_NAME = "Alice"
            const val ALICE_FCM_TOKEN = "alice-fcm-test-token"
            val ALICE_ICON = UserIcon.CAT

            const val BOB_ID = 2
            const val BOB_TOKEN = "bob-token"
            const val BOB_EMAIL = "bob@example.com"
            const val BOB_NAME = "Bob"
            const val BOB_FCM_TOKEN = "bob-fcm-test-token"
            val BOB_ICON = UserIcon.SHEEP

            val MALLORY_ANNIVERSARY: LocalDate = LocalDate.parse("2020-01-01")
            const val MALLORY_COUPLE_ID = 2

            const val MALLORY_ID = 3
            const val MALLORY_TOKEN = "mallory-token"
            const val MALLORY_EMAIL = "mallory@example.com"
            const val MALLORY_NAME = "Mallory"
            const val MALORY_FCM_TOKEN = "malory-fcm-test-token"
            val MALLORY_ICON = UserIcon.SHEEP

            const val INVALID_TOKEN = "invalid-token"
        }
    }

    companion object {
        @ServiceConnection
        @JvmStatic
        val mariadb =
            MariaDBContainer(DockerImageName.parse("mariadb:12"))
                .withDatabaseName("flamingo")
                .withUsername("flamingo-user")
                .withPassword("flamingo-user")
                .withInitScript("testdb.sql")

        init {
            mariadb.start()
        }
    }

    @BeforeEach
    fun setupAuth() {
        SecurityContextHolder.clearContext()

        // Setup correct test tokens
        whenever(firebaseAuth.verifyIdToken(TestData.ALICE_TOKEN))
            .thenReturn(aliceFirebaseToken)

        whenever(firebaseAuth.verifyIdToken(TestData.BOB_TOKEN))
            .thenReturn(bobFirebaseToken)

        whenever(firebaseAuth.verifyIdToken(TestData.MALLORY_TOKEN))
            .thenReturn(malloryFirebaseToken)

        // Setup incorrect test tokens
        val exception =
            FirebaseAuthException(
                FirebaseException(
                    ErrorCode.UNKNOWN,
                    "Invalid token",
                    Exception("Test INVALID_TOKEN"),
                ),
            )
        whenever(firebaseAuth.verifyIdToken(TestData.INVALID_TOKEN))
            .thenThrow(exception)
    }

    fun requestAlice(builder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder =
        builder.header("Authorization", "Bearer ${TestData.ALICE_TOKEN}")

    fun requestBob(builder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder =
        builder.header("Authorization", "Bearer ${TestData.BOB_TOKEN}")

    fun requestMallory(builder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder =
        builder.header("Authorization", "Bearer ${TestData.MALLORY_TOKEN}")

    fun testProtection(builder: MockHttpServletRequestBuilder) {
        mockMvc
            .perform(builder)
            .andExpect(status().isForbidden)
    }
}
