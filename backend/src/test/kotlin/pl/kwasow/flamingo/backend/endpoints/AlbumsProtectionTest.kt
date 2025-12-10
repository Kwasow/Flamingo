package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class AlbumsProtectionTest : BaseTest() {
    @Test
    fun `get endpoint is protected`() {
        testProtection(get("/albums/get"))
    }
}
