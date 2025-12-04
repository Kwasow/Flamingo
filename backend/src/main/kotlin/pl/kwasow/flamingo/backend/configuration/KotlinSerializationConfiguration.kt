package pl.kwasow.flamingo.backend.configuration

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ResolvableType
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Configuration(proxyBeanMethods = false)
class KotlinSerializationConfiguration {
    // ====== Beans
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        val json = Json
        val predicate = { _: ResolvableType? -> true }

        return KotlinSerializationJsonHttpMessageConverter(json, predicate)
    }
}
