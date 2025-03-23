package pl.kwasow.data.types

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

            val useIcons = user.icon != null && user.missingYouRecipient.icon != null
            val otherUser = user.missingYouRecipient

            return listOf(
                TabItem(
                    title = user.firstName,
                    icon = if (useIcons) user.icon?.res else null,
                    iconDescription = if (useIcons) user.icon?.description else null,
                    view = { WishlistView(person = user.firstName) },
                ),
                TabItem(
                    title = otherUser.firstName,
                    icon = if (useIcons) otherUser.icon?.res else null,
                    iconDescription = if (useIcons) otherUser.icon?.description else null,
                    view = { WishlistView(person = otherUser.firstName) },
                ),
            )
        }
    }
}
