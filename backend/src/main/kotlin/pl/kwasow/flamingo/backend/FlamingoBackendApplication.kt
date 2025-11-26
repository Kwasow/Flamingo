package pl.kwasow.flamingo.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan("pl.kwasow.flamingo")
class FlamingoBackendApplication

fun main(args: Array<String>) {
    runApplication<FlamingoBackendApplication>(*args)
}
