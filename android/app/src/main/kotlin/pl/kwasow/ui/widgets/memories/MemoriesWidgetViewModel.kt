package pl.kwasow.ui.widgets.memories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kwasow.data.types.Memory
import pl.kwasow.managers.MemoriesManager

class MemoriesWidgetViewModel(
    private val memoriesManager: MemoriesManager,
) : ViewModel() {
    // ====== Fields
    var areMemoriesLoading: Boolean by mutableStateOf(true)
        private set
    var todayMemories: List<Memory> by mutableStateOf(emptyList())
        private set
    var leadingMemories: List<Memory> by mutableStateOf(emptyList())
        private set
    private var photoMemories: List<Memory> by mutableStateOf(emptyList())

    // ====== Public methods
    fun loadMemories() {
        viewModelScope.launch {
            areMemoriesLoading = true
            photoMemories =
                memoriesManager.getMemories().values.flatten()
                    .filter { it.photo != null }
            todayMemories = memoriesManager.getTodayMemories()
            if (leadingMemories.isEmpty()) {
                leadingMemories = photoMemories.shuffled().take(5)
            }
            areMemoriesLoading = false
        }
    }
}
