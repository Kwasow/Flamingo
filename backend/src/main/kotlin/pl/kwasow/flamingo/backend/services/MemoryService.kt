package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.MemoryRepository
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User
import kotlin.jvm.optionals.getOrNull

@Service
class MemoryService(
    private val memoryRepository: MemoryRepository,
) {
    // ====== Public methods
    fun getMemoriesForUserByYear(user: User): Map<Int, List<Memory>> {
        val memories = memoryRepository.findByCoupleId(user.couple.id)
        val anniversary = user.couple.anniversary

        return memories.groupBy { memory ->
            val startDate = memory.startDate

            if (
                startDate.month <= anniversary.month
                && startDate.dayOfMonth < anniversary.dayOfMonth
            ) {
                startDate.year - 1
            } else {
                startDate.year
            }
        }
    }

    fun saveMemory(memory: Memory): Memory =
        memoryRepository.save(memory)

    fun deleteMemory(id: Int) =
        memoryRepository.deleteById(id)

    fun findOwner(memoryId: Int): Int? =
        memoryRepository.findById(memoryId).getOrNull()?.coupleId

}
