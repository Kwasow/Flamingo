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

    // User for add/update/delete
    var isOperationRunning: Boolean by mutableStateOf(false)
        private set
    var operationError: Boolean by mutableStateOf(false)
        private set

    var showYearPickerDialog: Boolean by mutableStateOf(false)
        private set
    var showAddMemoryDialog: Boolean by mutableStateOf(false)
        private set
    var memoryToEdit: Memory? by mutableStateOf(null)
        private set
    var memoryToDelete: Memory? by mutableStateOf(null)
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

    fun openYearPicker() {
        showYearPickerDialog = true
    }

    fun closeYearPicker() {
        showYearPickerDialog = false
    }

    fun setSelectedYear(year: Int) {
        if (!memories.containsKey(year)) {
            throw IllegalArgumentException(
                "Year $year is not present in memories (valid values: ${memories.keys})",
            )
        }

        currentYear = year
    }

    fun startAddingMemory() {
        showAddMemoryDialog = true
    }

    fun addMemory(memory: Memory) {
        viewModelScope.launch {
            isOperationRunning = true
            operationError = false

            if (memoriesManager.addMemory(memory)) {
                closeAddMemoryDialog()
                refreshMemories(true)
            } else {
                operationError = true
            }

            isOperationRunning = false
        }
    }

    fun closeAddMemoryDialog() {
        operationError = false
        showAddMemoryDialog = false
    }

    fun startEditingMemory(memory: Memory) {
        memoryToEdit = memory
    }

    fun updateMemory(memory: Memory) {
        viewModelScope.launch {
            isOperationRunning = true
            operationError = false

            if (memoriesManager.updateMemory(memory)) {
                closeEditMemoryDialog()
                refreshMemories(true)
            } else {
                operationError = true
            }

            isOperationRunning = false
        }
    }

    fun closeEditMemoryDialog() {
        operationError = false
        memoryToEdit = null
    }

    fun startDeletingMemory(memory: Memory) {
        memoryToDelete = memory
    }

    fun deleteMemory(memory: Memory) {
        viewModelScope.launch {
            isOperationRunning = true
            operationError = false

            if (memoriesManager.deleteMemory(memory.id)) {
                closeDeleteMemoryDialog()
                refreshMemories(true)
            } else {
                operationError = true
            }

            isOperationRunning = false
        }
    }

    fun closeDeleteMemoryDialog() {
        operationError = false
        memoryToDelete = null
    }
}
