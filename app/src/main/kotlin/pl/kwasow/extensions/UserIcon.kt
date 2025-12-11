package pl.kwasow.extensions

import pl.kwasow.R
import pl.kwasow.data.types.UserIconDetails
import pl.kwasow.flamingo.types.user.UserIcon

fun UserIcon.details() =
    when (this) {
        UserIcon.CAT -> UserIconDetails(R.drawable.ic_cat, R.string.contentDescription_cat_icon)
        UserIcon.SHEEP ->
            UserIconDetails(
                R.drawable.ic_sheep,
                R.string.contentDescription_sheep_icon,
            )
    }
