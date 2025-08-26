package pl.kwasow.flamingo.backend.services

import org.springframework.stereotype.Service
import pl.kwasow.flamingo.backend.repositories.MemoryRepository
import pl.kwasow.flamingo.types.memories.Memory

@Service
class MemoryService(
    private val memoryRepository: MemoryRepository,
) {
    fun getMemoriesForUser(): List<Memory> {

    }
}