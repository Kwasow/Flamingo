package pl.kwasow.data.types

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import pl.kwasow.extensions.details
import pl.kwasow.flamingo.types.user.User
import pl.kwasow.ui.screens.modules.whishlist.WishlistView

data class TabItem(
    val title: String,
    @DrawableRes val icon: Int?,
    @StringRes val iconDescription: Int?,
    val view: @Composable () -> Unit,
) {
    companion object {
        fun getWishlistTabs(user: User?): List<TabItem>? {
            if (user == null) {
                return null
            }

            val userIcon = user.icon.details()
            val tabs =
                mutableListOf(
                    TabItem(
                        title = user.firstName,
                        icon = userIcon.res,
                        iconDescription = userIcon.description,
                        view = { WishlistView(user = user) },
                    ),
                )

            user.partner?.let { partner ->
                val partnerIcon = partner.icon.details()

                tabs.add(
                    TabItem(
                        title = partner.firstName,
                        icon = partnerIcon.res,
                        iconDescription = partnerIcon.description,
                        view = { WishlistView(user = partner) },
                    ),
                )
            }

            return tabs
        }
    }
}
