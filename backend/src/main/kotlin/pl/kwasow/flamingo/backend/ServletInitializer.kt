package pl.kwasow.flamingo.backend

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class ServletInitializer : SpringBootServletInitializer() {
    // ====== Interface methods
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder =
        application.sources(FlamingoBackendApplication::class.java)
}
