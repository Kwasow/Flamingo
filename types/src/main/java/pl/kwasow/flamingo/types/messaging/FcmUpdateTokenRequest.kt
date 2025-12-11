package pl.kwasow.flamingo.types.messaging

import kotlinx.serialization.Serializable

@Serializable
data class FcmUpdateTokenRequest(
    val token: String,
    val debug: Boolean,
)
