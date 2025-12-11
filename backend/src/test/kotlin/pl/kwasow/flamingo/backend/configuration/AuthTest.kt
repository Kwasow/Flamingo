package pl.kwasow.flamingo.backend.configuration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.user.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class AuthTest : BaseTest() {
    @Test
    fun `endpoint is protected`() {
        val request = MockMvcRequestBuilders.get("/auth")

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `2 person couple request`() {
        val request = requestBob(MockMvcRequestBuilders.get("/auth"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk)
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

    @Test
    fun `1 person couple request`() {
        val request = requestMallory(MockMvcRequestBuilders.get("/auth"))

        val result =
            mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk)
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
