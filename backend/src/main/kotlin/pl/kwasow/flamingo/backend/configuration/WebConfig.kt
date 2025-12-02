package pl.kwasow.flamingo.backend.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverters
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun configureMessageConverters(builder: HttpMessageConverters.ServerBuilder) {
        builder
            .withJsonConverter(KotlinSerializationJsonHttpMessageConverter())
    }
}
