package pl.kwasow.flamingo.backend.endpoints

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {
    // ====== Endpoints
    @GetMapping("/ping")
    fun pingResponse(): String = "PONG"
}
