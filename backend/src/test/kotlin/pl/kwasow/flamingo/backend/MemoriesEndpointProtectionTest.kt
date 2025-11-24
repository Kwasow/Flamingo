package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class MemoriesEndpointProtectionTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `get endpoint is protected`() {
        val request = get("/memories/get")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)
    }

    @Transactional
    @Test
    fun `add endpoint is protected`() {
        val request = post("/memories/add")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)
    }

    @Transactional
    @Test
    fun `update endpoint is protected`() {
        val request = post("/memories/update")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)
    }

    @Transactional
    @Test
    fun `delete endpoint is protected`() {
        val request = delete("/memories/delete")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)
    }
}
