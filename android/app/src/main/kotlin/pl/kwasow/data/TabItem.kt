package pl.kwasow.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import pl.kwasow.ui.screens.modules.whishlist.WishlistView

data class TabItem(
    val title: String,
    @DrawableRes val icon: Int?,
    @StringRes val iconDescription: Int?,
    val view: @Composable () -> Unit,
) {
    companion object {
        fun getWishlistTabs(user: User?): List<TabItem> {
            if (user == null) {
                return emptyList()
            }

            val partner = user.partner

            return listOf(
                TabItem(
                    title = user.firstName,
                    icon = user.icon.res,
                    iconDescription = user.icon.description,
                    view = { WishlistView(person = user.firstName) },
                ),
                TabItem(
                    title = partner.firstName,
                    icon = partner.icon.res,
                    iconDescription = partner.icon.description,
                    view = { WishlistView(person = partner.firstName) },
                ),
            )
        }
    }
}
