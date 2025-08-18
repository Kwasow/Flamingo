package pl.kwasow.flamingo.types.auth

import pl.kwasow.flamingo.types.user.User

data class AuthenticationResult(
    val authorization: Authorization,
    val authenticatedUser: User?,
)
