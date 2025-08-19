package pl.kwasow.flamingo.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan("pl.kwasow.flamingo.types")
class FlamingoBackendApplication

fun main(args: Array<String>) {
    runApplication<FlamingoBackendApplication>(*args)
}
