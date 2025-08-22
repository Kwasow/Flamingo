package pl.kwasow.flamingo.backend.filters

import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class FirebaseTokenFilter(
    private val firebaseAuth: FirebaseAuth,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val idToken = authHeader.substring(7)

            try {
                val firebaseToken = firebaseAuth.verifyIdToken(idToken)
                val email = firebaseToken.email
                val authentication = UsernamePasswordAuthenticationToken(email, null, emptyList())

                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase Token")
                return
            }
        }

        filterChain.doFilter(request, response)
    }

}