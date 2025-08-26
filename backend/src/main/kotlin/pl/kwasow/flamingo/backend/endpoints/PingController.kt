package pl.kwasow.flamingo.backend.endpoints

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {
    @GetMapping("/ping")
    fun pingResponse() = ResponseEntity.ok("PONG")
}
