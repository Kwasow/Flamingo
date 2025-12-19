package pl.kwasow.flamingo.backend.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.MemoryRepository
import pl.kwasow.flamingo.types.memories.MemoryDto
import pl.kwasow.flamingo.types.user.User

@Service
class MemoryService(
    private val memoryRepository: MemoryRepository,
) {
    // ====== Public methods
    fun getMemoriesForUserByYear(user: User): Map<Int, List<MemoryDto>> {
        val memories = memoryRepository.findByCoupleId(user.couple.id)
        val anniversary = user.couple.anniversary

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

    fun saveMemory(memory: MemoryDto): MemoryDto = memoryRepository.save(memory)

    fun deleteMemory(memoryId: Int) = memoryRepository.deleteById(memoryId)

    fun findOwner(memoryId: Int): Int? = memoryRepository.findByIdOrNull(memoryId)?.coupleId

    fun verifyMemoryForAdding(memoryId: Int?): Boolean = memoryId == null

    fun verifyMemoryForEditing(
        user: User,
        memoryId: Int?,
    ): Boolean = memoryId != null && user.couple.id == findOwner(memoryId)
}
