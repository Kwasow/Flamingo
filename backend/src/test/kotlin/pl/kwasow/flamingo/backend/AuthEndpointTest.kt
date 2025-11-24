package pl.kwasow.flamingo.backend

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.user.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
@AutoConfigureMockMvc
class AuthEndpointTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Transactional
    @Test
    fun `endpoint is protected`() {
        val request = get("/auth")

        mockMvc
            .perform(request)
            .andExpect(status().isForbidden)
    }

    @Transactional
    @Test
    fun `2 person couple request`() {
        val request = requestBob(get("/auth"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()
        val user = json.decodeFromString<User>(result.response.contentAsString)

        assertEquals(TestData.BOB_EMAIL, user.email)
        assertEquals(TestData.BOB_NAME, user.firstName)
        assertEquals(TestData.BOB_ICON, user.icon)
        assertEquals(TestData.ALICE_BOB_ANNIVERSARY, user.couple.anniversary)
        assertEquals(2, user.couple.members.size)
        assertNotEquals(null, user.couple.members.find { it.firstName == TestData.ALICE_NAME })
        assertNotEquals(null, user.couple.members.find { it.firstName == TestData.BOB_NAME })
        assertNotEquals(null, user.couple.members.find { it.id == user.id })
    }

    @Transactional
    @Test
    fun `1 person couple request`() {
        val request = requestMallory(get("/auth"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(status().isOk)
                .andReturn()
        val user = json.decodeFromString<User>(result.response.contentAsString)

        assertEquals(TestData.MALLORY_EMAIL, user.email)
        assertEquals(TestData.MALLORY_NAME, user.firstName)
        assertEquals(TestData.MALLORY_ICON, user.icon)
        assertEquals(TestData.MALLORY_ANNIVERSARY, user.couple.anniversary)
        assertEquals(1, user.couple.members.size)
        assertNotEquals(null, user.couple.members.find { TestData.MALLORY_NAME == user.firstName })
        assertNotEquals(null, user.couple.members.find { it.id == user.id })
    }
}
