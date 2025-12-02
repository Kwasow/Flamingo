package pl.kwasow.flamingo.backend.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.kwasow.flamingo.backend.filters.FirebaseTokenFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val firebaseTokenFilter: FirebaseTokenFilter,
) {
    // ====== Public methods
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/ping")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterBefore(
                firebaseTokenFilter,
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }
}
