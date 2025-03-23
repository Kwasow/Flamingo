package pl.kwasow.data.types

data class AuthenticationResult(
    val authorization: Authorization,
    val authenticatedUser: User?,
) {
    enum class Authorization {
        AUTHORIZED,
        UNAUTHORIZED,
        UNKNOWN,
    }
}
