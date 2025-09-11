package pl.kwasow.flamingo.backend.configuration

import com.google.firebase.auth.FirebaseAuth
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.kwasow.flamingo.backend.filters.FirebaseTokenFilter
import pl.kwasow.flamingo.backend.services.UserService

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val firebaseAuth: FirebaseAuth,
    private val userService: UserService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/ping").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(FirebaseTokenFilter(firebaseAuth, userService),
                UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
