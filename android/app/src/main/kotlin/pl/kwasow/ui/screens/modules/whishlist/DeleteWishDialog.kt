package pl.kwasow.ui.screens.modules.whishlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.types.Wish

// ====== Public composables
@Composable
fun DeleteWishDialog() {
    val viewModel = koinViewModel<WishlistModuleViewModel>()
    val wish = viewModel.wishToDelete

    if (wish != null) {
        DeleteWishDialog(
            wish = wish,
            buttonsEnabled = !viewModel.deletingWish,
            onConfirm = { viewModel.confirmDeleteWish() },
            onCancel = { viewModel.cancelDeleteWish() },
        )
    }
}

// ====== Private composables
@Composable
fun DeleteWishDialog(
    wish: Wish,
    buttonsEnabled: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        icon = { AlertIcon() },
        title = { AlertTitle() },
        text = { AlertContent(content = wish.content) },
        confirmButton = {
            AlertConfirmButton(
                enabled = buttonsEnabled,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            AlertDismissButton(
                enabled = buttonsEnabled,
                onClick = onCancel,
            )
        },
        onDismissRequest = onCancel,
    )
}

// ====== Private composables
@Composable
private fun AlertIcon() {
    Icon(
        imageVector = Icons.Outlined.Delete,
        contentDescription = stringResource(id = R.string.contentDescription_trash_icon),
    )
}

@Composable
private fun AlertTitle() {
    Text(
        text = stringResource(id = R.string.module_wishlist_delete_dialog_header),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertContent(content: String) {
    Text(
        text = stringResource(id = R.string.module_wishlist_delete_dialog_text, content),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.delete))
    }
}

@Composable
private fun AlertDismissButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.cancel))
    }
}

// ====== Previews
@Preview
@Composable
private fun DeleteWishDialogPreview() {
    val wish =
        Wish(
            id = 0,
            authorId = 123,
            content = "This is a link to https://google.com",
            timestamp = 0,
            done = true,
        )

    DeleteWishDialog(
        wish = wish,
        buttonsEnabled = true,
        onConfirm = {},
        onCancel = {},
    )
}

@Preview
@Composable
private fun DeleteWishDialogPreviewButtonsDisabled() {
    val wish =
        Wish(
            id = 0,
            authorId = 123,
            content = "This is a link to https://google.com",
            timestamp = 0,
            done = true,
        )

    DeleteWishDialog(
        wish = wish,
        buttonsEnabled = false,
        onConfirm = {},
        onCancel = {},
    )
}
