package pl.kwasow.managers

import pl.kwasow.flamingo.types.memories.Memory
import java.time.LocalDate

interface MemoriesManager {
    // ====== Methods
    suspend fun getMemories(forceRefresh: Boolean = false): Map<Int, List<Memory>>

    suspend fun getTodayMemories(): List<Memory>

    suspend fun addMemory(
        startDate: LocalDate,
        endDate: LocalDate?,
        title: String,
        description: String,
    ): Boolean
}
