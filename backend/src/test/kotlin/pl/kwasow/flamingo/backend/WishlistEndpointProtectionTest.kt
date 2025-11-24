package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class WishlistEndpointProtectionTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `get endpoint is protected`() {
        testProtection(mockMvc, get("/wishlist/get"))
    }

    @Transactional
    @Test
    fun `add endpoint is protected`() {
        testProtection(mockMvc, post("/wishlist/add"))
    }

    @Transactional
    @Test
    fun `update endpoint is protected`() {
        testProtection(mockMvc, post("/wishlist/update"))
    }

    @Transactional
    @Test
    fun `delete endpoint is protected`() {
        testProtection(mockMvc, delete("/wishlist/delete"))
    }
}
