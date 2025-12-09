package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class WishlistEndpointProtectionTest : BaseTest() {
    @Test
    fun `get endpoint is protected`() {
        testProtection(get("/wishlist/get"))
    }

    @Test
    fun `add endpoint is protected`() {
        testProtection(post("/wishlist/add"))
    }

    @Test
    fun `update endpoint is protected`() {
        testProtection(post("/wishlist/update"))
    }

    @Test
    fun `delete endpoint is protected`() {
        testProtection(delete("/wishlist/delete"))
    }
}
