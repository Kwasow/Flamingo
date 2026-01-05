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

    var isSaving: Boolean by mutableStateOf(false)
    var savingError: Boolean by mutableStateOf(false)
    var showYearPickerDialog: Boolean by mutableStateOf(false)
    var showAddMemoryDialog: Boolean by mutableStateOf(false)
    var editedMemory: Memory? by mutableStateOf(null)

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

    fun closeDialogs() {
        savingError = false

        showAddMemoryDialog = false
        editedMemory = null
    }

    fun addMemory(memory: Memory) {
        viewModelScope.launch {
            isSaving = true

            if (memoriesManager.addMemory(memory)) {
                closeDialogs()
                refreshMemories(true)
            } else {
                savingError = true
            }

            isSaving = false
        }
    }

    fun updateMemory(memory: Memory) {
        viewModelScope.launch {
            isSaving = true

            if (memoriesManager.updateMemory(memory)) {
                closeDialogs()
                refreshMemories(true)
            } else {
                savingError = true
            }

            isSaving = false
        }
    }
}
