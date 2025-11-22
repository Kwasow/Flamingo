package pl.kwasow.flamingo.backend.base

import com.google.firebase.FirebaseApp
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mariadb.MariaDBContainer
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
abstract class BaseTest {

    @MockitoBean
    private lateinit var firebaseApp: FirebaseApp

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mariadb = MariaDBContainer(DockerImageName.parse("mariadb:12"))
            .withDatabaseName("flamingo")
            .withUsername("flamingo-user")
            .withPassword("flamingo-user")
            .withInitScript("testdb.sql")
    }

}
