package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.DeleteDialog
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel

@Composable
fun DialogMemoriesDelete() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()
    val memory = viewModel.memoryToDelete

    if (memory != null) {
        DeleteDialog(
            title = stringResource(id = R.string.module_memories_delete_dialog_header),
            content = memory.title,
            isEnabled = !viewModel.isOperationRunning,
            isError = viewModel.operationError,
            onConfirm = { viewModel.deleteMemory(memory) },
            onCancel = { viewModel.closeDeleteMemoryDialog() },
        )
    }
}
