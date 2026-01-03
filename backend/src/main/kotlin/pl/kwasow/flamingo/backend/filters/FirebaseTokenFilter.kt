package pl.kwasow.flamingo.backend.filters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.kwasow.flamingo.backend.services.UserService
import pl.kwasow.flamingo.types.user.User

@Component
class FirebaseTokenFilter(
    private val firebaseAuth: FirebaseAuth,
    private val userService: UserService,
) : OncePerRequestFilter() {
    // ====== Interface methods
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val bearerToken = getBearerToken(request)

        if (bearerToken != null) {
            try {
                val firebaseToken = firebaseAuth.verifyIdToken(bearerToken)
                val email = firebaseToken.email

                val user = userService.getUserByEmail(email)

                if (user == null) {
                    throw AuthenticationCredentialsNotFoundException("User not found")
                } else {
                    val authentication =
                        UsernamePasswordAuthenticationToken(User(user), null, emptyList())
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (_: FirebaseAuthException) {
                SecurityContextHolder.clearContext()
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase Token")

                return
            } catch (_: AuthenticationCredentialsNotFoundException) {
                SecurityContextHolder.clearContext()
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not registered")

                return
            } catch (e: Exception) {
                SecurityContextHolder.clearContext()
                response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error",
                )

                logger.error(e)
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    // ====== Private methods
    private fun getBearerToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7)
        }

        return null
    }
}
