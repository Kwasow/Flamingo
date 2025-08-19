package pl.kwasow.ui.screens.modules.memories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.managers.MemoriesManager

class MemoriesModuleViewModel(
    private val memoriesManager: MemoriesManager,
) : ViewModel() {
    // ====== Fields
    var areMemoriesLoading: Boolean by mutableStateOf(true)
        private set
    var memories: Map<Int, List<Memory>> by mutableStateOf(emptyMap())
        private set
    var currentYear: Int by mutableIntStateOf(-1)
        private set
    var memoriesLoaded: Boolean by mutableStateOf(false)
        private set

    // ====== Constructors
    init {
        refreshMemories()
    }

    // ====== Public methods
    fun refreshMemories(force: Boolean = false) {
        viewModelScope.launch {
            areMemoriesLoading = true
            memories = memoriesManager.getMemories(force)
            currentYear = if (memories.isEmpty()) -1 else memories.keys.maxOf { it }
            areMemoriesLoading = false
            memoriesLoaded = true
        }
    }

    fun setSelectedYear(year: Int) {
        if (!memories.containsKey(year)) {
            throw IllegalArgumentException(
                "Year $year is not present in memories (valid values: ${memories.keys})",
            )
        }

        currentYear = year
    }
}
