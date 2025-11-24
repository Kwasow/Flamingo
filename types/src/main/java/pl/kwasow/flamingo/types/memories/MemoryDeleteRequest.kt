package pl.kwasow.flamingo.types.memories

import kotlinx.serialization.Serializable

@Serializable
data class MemoryDeleteRequest(
    val id: Int,
)
