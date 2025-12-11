package pl.kwasow.flamingo.types.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserIcon {
    @SerialName("CAT")
    CAT,

    @SerialName("SHEEP")
    SHEEP,
}
