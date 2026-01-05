package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.memories.MemoryDto

interface MemoryRepository : JpaRepository<MemoryDto, Int> {
    // ====== Public methods
    fun findByCoupleIdOrderByStartDate(coupleId: Int): List<MemoryDto>
}
