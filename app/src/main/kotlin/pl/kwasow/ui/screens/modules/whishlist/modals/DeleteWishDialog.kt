package pl.kwasow.ui.screens.modules.whishlist.modals

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.DeleteDialog
import pl.kwasow.ui.screens.modules.whishlist.WishlistModuleViewModel

// ====== Public composables
@Composable
fun DeleteWishDialog() {
    val viewModel = koinViewModel<WishlistModuleViewModel>()
    val wish = viewModel.wishToDelete

    if (wish != null) {
        DeleteDialog(
            title = stringResource(id = R.string.module_wishlist_delete_dialog_header),
            content = wish.content,
            buttonsEnabled = !viewModel.deletingWish,
            onConfirm = { viewModel.confirmDeleteWish() },
            onCancel = { viewModel.cancelDeleteWish() },
        )
    }
}
