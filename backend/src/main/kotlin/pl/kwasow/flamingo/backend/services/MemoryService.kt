package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.MemoryRepository
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User

@Service
class MemoryService(
    private val memoryRepository: MemoryRepository,
) {
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

    fun addUpdateMemory(memory: Memory) {
        memoryRepository.save(memory)
    }
}
