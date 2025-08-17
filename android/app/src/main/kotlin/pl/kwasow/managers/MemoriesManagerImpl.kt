package pl.kwasow.managers

import pl.kwasow.flamingo.types.Memory
import java.time.LocalDate

class MemoriesManagerImpl(
    private val requestManager: RequestManager,
    private val systemManager: SystemManager,
) : MemoriesManager {
    // ====== Fields
    private var cachedMemories: Map<Int, List<Memory>>? = null

    // ====== Interface methods
    override suspend fun getMemories(forceRefresh: Boolean): Map<Int, List<Memory>> {
        if (forceRefresh && systemManager.isInternetAvailable()) {
            cachedMemories = null
        }

        val memories =
            cachedMemories
                ?: requestManager.getMemories()
                ?: systemManager.getCachedMemories()
        systemManager.cacheMemories(memories)
        cachedMemories = memories

        return memories ?: emptyMap()
    }

    override suspend fun getTodayMemories(): List<Memory> {
        val memories = getMemories()
        val todayMemories = mutableListOf<Memory>()

        memories.values.flatten().forEach { memory ->
            val today = LocalDate.now()
            val memoryDate = memory.getLocalStartDate()

            if (
                today.month == memoryDate?.month &&
                today.dayOfMonth == memoryDate?.dayOfMonth
            ) {
                todayMemories.add(memory)
            }
        }

        return todayMemories
    }
}
