package pl.kwasow.data.types

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UserIconDetails(
    @DrawableRes val res: Int,
    @StringRes val description: Int,
)
