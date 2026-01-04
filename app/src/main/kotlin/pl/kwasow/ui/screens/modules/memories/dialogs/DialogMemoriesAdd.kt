package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel
import java.time.LocalDate

// ====== Public composables
@Composable
fun DialogMemoriesAdd() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    if (viewModel.showAddMemoryDialog) {
        DialogMemoriesAdd(
            onConfirm = {
                viewModel.addMemory(it)
                viewModel.showAddMemoryDialog = false
            },
            onCancel = { viewModel.showAddMemoryDialog = false },
        )
    }
}

// ====== Private composables
@Composable
private fun DialogMemoriesAdd(
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
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
        imageVector = Icons.Outlined.PostAdd,
        contentDescription = "TODO",
    )
}

@Composable
private fun AlertTitle() {
    Text(
        text = stringResource(id = R.string.module_memories_add_memory),
        textAlign = TextAlign.Center,
    )
}
