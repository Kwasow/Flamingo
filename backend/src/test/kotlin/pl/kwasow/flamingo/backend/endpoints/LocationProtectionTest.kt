package pl.kwasow.flamingo.backend.endpoints

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pl.kwasow.flamingo.backend.setup.BaseTest
import kotlin.test.Test

@SpringBootTest
class LocationProtectionTest : BaseTest() {
    @Test
    fun `get partner is protected`() {
        testProtection(get("/location/get/partner"))
    }

    @Test
    fun `get self is protected`() {
        testProtection(get("/location/get/self"))
    }

    @Test
    fun `update is protected`() {
        testProtection(post("/location/update"))
    }
}
