package pl.kwasow.flamingo.types.user

import kotlinx.serialization.Serializable

@Serializable
data class Partner(
    override val id: Int,
    override val firstName: String,
    override val icon: UserIcon,
) : MinimalUser
