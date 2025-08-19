package pl.kwasow.flamingo.types.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    override val id: Int,
    override val firstName: String,
    val email: String,
    override val icon: UserIcon,
    val partner: Partner,
    val coupleDetails: CoupleDetails,
) : MinimalUser
