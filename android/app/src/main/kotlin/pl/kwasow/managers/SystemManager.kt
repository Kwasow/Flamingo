package pl.kwasow.managers

import pl.kwasow.data.types.Memory
import pl.kwasow.data.types.User

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
