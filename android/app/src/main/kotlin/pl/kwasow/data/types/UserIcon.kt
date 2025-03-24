package pl.kwasow.data.types

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.kwasow.R

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
