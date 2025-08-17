package pl.kwasow.flamingo.types.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserIcon {
    @SerialName("cat")
    CAT,

    @SerialName("sheep")
    SHEEP,
}
