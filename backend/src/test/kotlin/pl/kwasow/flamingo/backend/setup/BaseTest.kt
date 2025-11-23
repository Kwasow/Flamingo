package pl.kwasow.flamingo.backend.setup

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.testcontainers.mariadb.MariaDBContainer
import org.testcontainers.utility.DockerImageName
import pl.kwasow.flamingo.types.user.UserIcon
import java.time.LocalDate

@SpringBootTest
abstract class BaseTest {
    @MockitoBean
    private lateinit var firebaseApp: FirebaseApp

    @MockitoBean
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var bobFirebaseToken: FirebaseToken

    @Mock
    private lateinit var aliceFirebaseToken: FirebaseToken

    @Mock
    private lateinit var malloryFirebaseToken: FirebaseToken

    protected val json = Json

    protected class TestData {
        companion object {
            val ALICE_BOB_ANNIVERSARY: LocalDate = LocalDate.parse("2023-07-31")

            const val ALICE_TOKEN = "alice-token"
            const val ALICE_EMAIL = "alice@example.com"
            const val ALICE_NAME = "Alice"
            val ALICE_ICON = UserIcon.CAT

            const val BOB_TOKEN = "bob-token"
            const val BOB_EMAIL = "bob@example.com"
            const val BOB_NAME = "Bob"
            val BOB_ICON = UserIcon.SHEEP

            val MALLORY_ANNIVERSARY: LocalDate = LocalDate.parse("2020-01-01")

            const val MALLORY_TOKEN = "mallory-token"
            const val MALLORY_EMAIL = "mallory@example.com"
            const val MALLORY_NAME = "Mallory"
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
    fun setup() {
        SecurityContextHolder.clearContext()

        // Setup correct test tokens
        whenever(firebaseAuth.verifyIdToken(TestData.ALICE_TOKEN))
            .thenReturn(aliceFirebaseToken)
        whenever(aliceFirebaseToken.email)
            .thenReturn(TestData.ALICE_EMAIL)

        whenever(firebaseAuth.verifyIdToken(TestData.BOB_TOKEN))
            .thenReturn(bobFirebaseToken)
        whenever(bobFirebaseToken.email)
            .thenReturn(TestData.BOB_EMAIL)

        whenever(firebaseAuth.verifyIdToken(TestData.MALLORY_TOKEN))
            .thenReturn(malloryFirebaseToken)
        whenever(malloryFirebaseToken.email)
            .thenReturn(TestData.MALLORY_EMAIL)

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

    fun getAlice(uriTemplate: String) =
        get(uriTemplate).header("Authorization", "Bearer ${TestData.ALICE_TOKEN}")

    fun getBob(uriTemplate: String) =
        get(uriTemplate).header("Authorization", "Bearer ${TestData.BOB_TOKEN}")

    fun getMallory(uriTemplate: String) =
        get(uriTemplate).header("Authorization", "Bearer ${TestData.MALLORY_TOKEN}")
}
