package pl.kwasow.flamingo.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlamingoBackendApplication

fun main(args: Array<String>) {
    runApplication<FlamingoBackendApplication>(*args)
}
