package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel

// ====== Public composables
@Composable
fun BottomSheetMemoriesEdit() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    viewModel.editedMemory?.let { memory ->
        BottomSheetMemoriesEdit(
            initialMemory = memory,
            onConfirm = { viewModel.updateMemory(it) },
            onCancel = { viewModel.editedMemory = null },
        )
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetMemoriesEdit(
    initialMemory: Memory,
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
) {
    BottomSheetMemoriesShared(
        initialMemory = initialMemory,
        title = stringResource(id = R.string.module_memories_update_memory),
        onConfirm = onConfirm,
        onCancel = onCancel,
    )
}
