package pl.kwasow.managers

import pl.kwasow.flamingo.types.memories.Memory

interface MemoriesManager {
    // ====== Methods
    suspend fun getMemories(forceRefresh: Boolean = false): Map<Int, List<Memory>>

    suspend fun getTodayMemories(): List<Memory>
}
