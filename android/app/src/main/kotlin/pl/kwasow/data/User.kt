package pl.kwasow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.kwasow.R

interface MinimalUser {
    val id: Int
    val firstName: String
    val icon: UserIcon?
}

@Serializable
data class MissingYouRecipient(
    override val id: Int,
    override val firstName: String,
    override val icon: UserIcon,
) : MinimalUser

@Serializable
data class User(
    override val id: Int,
    override val firstName: String,
    val lastName: String,
    val email: String,
    override val icon: UserIcon,
    val missingYouRecipient: MissingYouRecipient,
) : MinimalUser

@Serializable
enum class UserIcon(
    @DrawableRes val res: Int,
    @StringRes val description: Int,
) {
    @SerialName("cat")
    CAT(R.drawable.ic_cat, R.string.contentDescription_cat_icon),

    @SerialName("sheep")
    SHEEP(R.drawable.ic_sheep, R.string.contentDescription_sheep_icon),
}
