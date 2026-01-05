package pl.kwasow.managers

import pl.kwasow.flamingo.types.memories.Memory
import java.time.LocalDate

class MemoriesManagerImpl(
    private val requestManager: RequestManager,
    private val systemManager: SystemManager,
    private val userManager: UserManager,
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
                ?: loadMemoriesFromServer()
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

            if (
                today.month == memory.startDate.month &&
                today.dayOfMonth == memory.startDate.dayOfMonth
            ) {
                todayMemories.add(memory)
            }
        }

        return todayMemories
    }

    override suspend fun addMemory(memory: Memory): Boolean = requestManager.addMemory(memory)

    override suspend fun updateMemory(memory: Memory): Boolean = requestManager.updateMemory(memory)

    // ====== Private methods
    private suspend fun loadMemoriesFromServer(): Map<Int, List<Memory>>? {
        val memories = requestManager.getMemories() ?: return null
        val anniversary =
            userManager.user.value
                ?.couple
                ?.anniversary ?: return null

        return memories.groupBy { memory ->
            val startDate = memory.startDate

            if (
                startDate.month <= anniversary.month &&
                startDate.dayOfMonth < anniversary.dayOfMonth
            ) {
                startDate.year - 1
            } else {
                startDate.year
            }
        }
    }

    override suspend fun deleteMemory(id: Int?): Boolean =
        id != null && requestManager.deleteMemory(id)
}
