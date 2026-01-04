package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel

// ====== Public composables
@Composable
fun DialogMemoriesEdit() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    viewModel.editedMemory?.let { memory ->
        DialogMemoriesEdit(
            initialMemory = memory,
            onConfirm = { viewModel.updateMemory(it) },
            onCancel = { viewModel.editedMemory = null },
        )
    }
}

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
        text = stringResource(id = R.string.module_memories_update_memory),
        textAlign = TextAlign.Center,
    )
}
