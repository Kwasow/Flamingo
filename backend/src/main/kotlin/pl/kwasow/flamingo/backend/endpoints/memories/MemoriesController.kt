package pl.kwasow.flamingo.backend.endpoints.memories

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.MemoryService
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User

@RestController
class MemoriesController(
    private val memoryService: MemoryService,
) {
    @GetMapping("/memories/get")
    fun getMemories(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Map<Int, List<Memory>>> {
        return ResponseEntity.ok(memoryService.getMemoriesForUserByYear(user))
    }

    @PostMapping("/memories/add")
    fun addMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory
    ): ResponseEntity<Memory> {
        val incomingMemory = memory.copy(id = null, coupleId = user.couple.id)
        val newMemory = memoryService.addUpdateMemory(incomingMemory)

        return ResponseEntity
            .ok()
            .body(newMemory)
    }

    @PostMapping("/memories/edit")
    fun editMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory
    ): ResponseEntity<Memory> {
        // TODO: [SEC] Check if it is a user's couple's memory
        val incomingMemory = memory.copy(coupleId = user.couple.id)
        val editedMemory = memoryService.addUpdateMemory(incomingMemory)

        return ResponseEntity
            .ok()
            .body(editedMemory)
    }
}
