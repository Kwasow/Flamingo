package pl.kwasow.managers

import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User

interface SystemManager {
    // ====== Methods
    fun isInternetAvailable(): Boolean

    fun cacheMemories(memories: Map<Int, List<Memory>>?)

    fun getCachedMemories(): Map<Int, List<Memory>>?

    fun cacheUser(user: User?)

    fun getCachedUser(): User?

    fun clearCache()

    fun clearCoilCache()

    fun launchStore()
}
