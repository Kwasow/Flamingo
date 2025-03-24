package pl.kwasow.data.types

import kotlinx.serialization.Serializable

@Serializable
data class Partner(
    override val id: Int,
    override val firstName: String,
    override val icon: UserIcon,
) : MinimalUser
