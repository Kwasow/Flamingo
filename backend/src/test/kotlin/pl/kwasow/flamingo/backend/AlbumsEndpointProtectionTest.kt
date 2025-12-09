package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class AlbumsEndpointProtectionTest : BaseTest() {
    @Test
    fun `get endpoint is protected`() {
        testProtection(get("/albums/get"))
    }
}
