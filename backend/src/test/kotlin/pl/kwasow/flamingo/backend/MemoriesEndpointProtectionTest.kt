package pl.kwasow.flamingo.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class MemoriesEndpointProtectionTest : BaseTest() {
    @Test
    fun `get endpoint is protected`() {
        testProtection(mockMvc, get("/memories/get"))
    }

    @Test
    fun `add endpoint is protected`() {
        testProtection(mockMvc, post("/memories/add"))
    }

    @Test
    fun `update endpoint is protected`() {
        testProtection(mockMvc, post("/memories/update"))
    }

    @Test
    fun `delete endpoint is protected`() {
        testProtection(mockMvc, delete("/memories/delete"))
    }
}
