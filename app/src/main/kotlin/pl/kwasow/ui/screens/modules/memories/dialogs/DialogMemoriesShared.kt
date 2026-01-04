package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory

// ====== Public composables
@Composable
fun DialogMemoriesShared(
    initialMemory: Memory,
    icon:  @Composable (() -> Unit),
    title:  @Composable (() -> Unit),
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
) {
    val buttonsEnabled = true

    AlertDialog(
        icon = icon,
        title = title,
        text = { AlertContent(content = "TODO") },
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
private fun AlertContent(content: String) {
    Text(
        text = stringResource(id = R.string.module_wishlist_delete_dialog_text, content),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onClick: (Memory) -> Unit,
) {
    TextButton(
        onClick = { },
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.add))
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
