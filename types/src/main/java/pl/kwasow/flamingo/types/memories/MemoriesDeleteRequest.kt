package pl.kwasow.flamingo.types.memories

import kotlinx.serialization.Serializable

@Serializable
data class MemoriesDeleteRequest(
    val id: Int,
)
