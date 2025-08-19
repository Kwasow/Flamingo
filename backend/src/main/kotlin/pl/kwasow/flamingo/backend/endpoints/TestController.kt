package pl.kwasow.flamingo.backend.endpoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.repositories.CoupleRepository
import pl.kwasow.flamingo.backend.repositories.UserRepository
import pl.kwasow.flamingo.types.user.Couple
import pl.kwasow.flamingo.types.user.User

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired
    lateinit var coupleRepository: CoupleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping
    fun error(): String = "Try /test/{tableName}"

    @GetMapping("/couples")
    fun getCouples(): List<Couple> = coupleRepository.findAll()

    @GetMapping("/users")
    fun getUsers(): List<User> = userRepository.findAll()
}
