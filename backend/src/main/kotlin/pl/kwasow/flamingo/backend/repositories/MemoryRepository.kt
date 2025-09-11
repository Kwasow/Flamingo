package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.memories.Memory

interface MemoryRepository : JpaRepository<Memory, Int> {
    fun findByCoupleId(coupleId: Int): List<Memory>
}
