package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMemoriesEdit() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    viewModel.memoryToEdit?.let { memory ->
        BottomSheetMemoriesShared(
            initialMemory = memory,
            title = stringResource(id = R.string.module_memories_update_memory),
            onConfirm = { viewModel.updateMemory(it) },
            onCancel = { viewModel.closeEditMemoryDialog() },
            isSaving = viewModel.isOperationRunning,
            isError = viewModel.operationError,
        )
    }
}
