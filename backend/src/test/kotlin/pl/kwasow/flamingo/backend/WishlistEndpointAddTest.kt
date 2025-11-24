package pl.kwasow.flamingo.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.kwasow.flamingo.backend.setup.BaseTest

@SpringBootTest
@AutoConfigureMockMvc
class WishlistEndpointAddTest : BaseTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

//    @Transactional
//    @Test
//    fun `bob can add own wish`() {
//
//    }
//
//    @Transactional
//    @Test
//    fun `bob can add alice's wish`() {
//
//    }
//
//    @Transactional
//    @Test
//    fun `mallory can't add bob's wish`() {
//
//    }
//
//    @Transactional
//    @Test
//    fun `mallory can't add alice's wish`() {
//
//    }
}
