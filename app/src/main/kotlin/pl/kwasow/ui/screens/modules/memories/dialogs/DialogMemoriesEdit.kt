package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import pl.kwasow.flamingo.types.memories.Memory

// ====== Public composables
// @Composable
// fun DialogMemoriesEdit() {
//    val viewModel = koinViewModel<MemoriesModuleViewModel>()
//
//    if (viewModel.showEditMemoryDialog) {
//        DialogMemoriesEdit(
//            initialMemory = null,
//            onConfirm = {},
//            onCancel = { viewModel.showEditMemoryDialog = false },
//        )
//    }
// }

// ====== Private composables
@Composable
private fun DialogMemoriesEdit(
    initialMemory: Memory,
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
) {
    DialogMemoriesShared(
        initialMemory = initialMemory,
        icon = { AlertIcon() },
        title = { AlertTitle() },
        onConfirm = onConfirm,
        onCancel = onCancel,
    )
}

@Composable
private fun AlertIcon() {
    Icon(
        imageVector = Icons.Outlined.EditNote,
        contentDescription = "TODO",
    )
}

@Composable
private fun AlertTitle() {
    Text(
        text = "TODO",
        textAlign = TextAlign.Center,
    )
}
