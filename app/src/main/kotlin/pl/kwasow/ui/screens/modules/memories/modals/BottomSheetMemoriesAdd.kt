package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel
import java.time.LocalDate

// ====== Public composables
@Composable
fun BottomSheetMemoriesAdd() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    if (viewModel.showAddMemoryDialog) {
        BottomSheetMemoriesAdd(
            onConfirm = { viewModel.addMemory(it) },
            onCancel = { viewModel.closeDialogs() },
            error = viewModel.savingError,
        )
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetMemoriesAdd(
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
    error: Boolean,
) {
    val initialMemory =
        Memory(
            id = null,
            startDate = LocalDate.now(),
            endDate = null,
            title = "",
            description = "",
            photo = null,
        )

    BottomSheetMemoriesShared(
        initialMemory = initialMemory,
        title = stringResource(id = R.string.module_memories_add_memory),
        onConfirm = onConfirm,
        onCancel = onCancel,
        error = error,
    )
}
