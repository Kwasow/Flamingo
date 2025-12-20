package pl.kwasow.flamingo.backend.configuration

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Configuration
import org.springframework.core.ResolvableType
import org.springframework.http.converter.HttpMessageConverters
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfiguration : WebMvcConfigurer {
    // ====== Interface methods
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/resources/**")
            .addResourceLocations("file:/resources/")
    }

    override fun configureMessageConverters(builder: HttpMessageConverters.ServerBuilder) {
        builder.withJsonConverter(buildKotlinxJsonConverter())
    }

    // ====== Private methods
    private fun buildKotlinxJsonConverter(): KotlinSerializationJsonHttpMessageConverter {
        val json = Json
        val predicate = { _: ResolvableType? -> true }

        return KotlinSerializationJsonHttpMessageConverter(json, predicate)
    }
}
